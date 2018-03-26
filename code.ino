#include <Wire.h>
#include <dht11.h>
#include <SoftwareSerial.h>
#include <avr/sleep.h>
#include <avr/wdt.h>

dht11 DHT11;
#define DHT11PIN 2 //DHT11 PIN 2 connect UNO 3
int BH1750address = 0x23;
SoftwareSerial wifi(10, 11); // RX, TX
String comdata = ""; //get from wifi rx,tx
String devNum = "asd12";
int temperature = 0, humidity = 0;
int pm25 = 0;
int lx = 0;
int sleepData = 0;

void setup() {
  setup_watchdog();
  ACSR |= _BV(ACD); //OFF ACD
  ADCSRA = 0; //OFF ADC
  Sleep_avr();//Sleep_Mode
  Wire.begin();
  wifi.begin(9600); //esp8266
  Serial.begin(2400); //GP2Y use 2400
}

void loop() {
  if (sleepData >= 45) {
    sleepData = 0;
    Serial.println("================loop==================");
    delay(500);
    readTH();
    getLx(BH1750address);
    readPm25();
    delay(1000);
    postHTTP();
    Serial.print("temp:");
    Serial.println(temperature);
    Serial.print("humi:");
    Serial.println(humidity);
    Serial.print("pm25:");
    Serial.println(pm25);
    Serial.print("lx:");
    Serial.println(lx);
    Serial.println();
    delay(500);
    Sleep_avr();
  } else {
    Sleep_avr();
  }
}

void readTH() {
  int chk = DHT11.read(DHT11PIN);
  if (chk == DHTLIB_OK) {
    temperature = DHT11.temperature;
    humidity = DHT11.humidity;
  }
}

void readPm25() {
  int incomeByte[7];
  int data; //read from GP2Y
  int z = 0; //GP2Y [7]
  int ret = 0;
  int cnt = 0;
  int all = 0;
  int sum = 0;
  while (Serial.read() >= 0);
  delay(30);
//  Serial.print("num:");
//  Serial.println(Serial.available());
  while (Serial.available() > 0) {
    delay(10);
    data = Serial.read();
    if (data == 170) {
      z = 0;
      incomeByte[z] = data;
    }
    else {
      z++;
      incomeByte[z] = data;
    }
    if (z == 6)
    {
      sum = incomeByte[1] + incomeByte[2] + incomeByte[3] + incomeByte[4];
      if (incomeByte[5] == sum && incomeByte[6] == 255 )
      {
        ret = (incomeByte[1] * 256.0 + incomeByte[2]) / 1024.0 * 5.00 * 550;
//        Serial.println(ret);
//        for (int i = 0; i < 7; i++) {
//          Serial.print(incomeByte[i]);
//          Serial.print(" ");
//        }
//        Serial.println();
        all = all + ret;
        cnt++;
        if (cnt >= 5) {
          pm25 = all / cnt;
          break;
        }
      } else {
        Serial.println("sum error");
      }

    }
  }
}

void getLx(int address) {
  //init
  Wire.beginTransmission(address);
  Wire.write(0x10);//1lx reolution 120ms
  Wire.endTransmission();
  delay(200);
  //read
  byte buff[2];
  Wire.beginTransmission(address);
  Wire.requestFrom(address, 2);
  int i = 0;
  while (Wire.available()) //
  {
    buff[i] = Wire.read();  // receive one byte
    i++;
  }
  Wire.endTransmission();
  //format
  lx = ((buff[0] << 8) | buff[1]) / 1.2;
}

//read wifi rx,tx
void getComdata()
{
  while (wifi.available() > 0) {
    comdata += char(wifi.read());
    delay(2);
  }
}

void postHTTP()
{
  getComdata();
  if (comdata.length() > 0) {
    Serial.println(comdata);
    comdata = "";
  }
  delay(1000);
  wifi.println("AT+CIPSTART=\"TCP\",\"pj.zhangtory.com\",80");
  delay(6000);
  getComdata();
  if (comdata.length() > 0) {
    Serial.println(comdata);
    comdata = "";
  }
  //send date to 8266
  wifi.println("AT+CIPSEND");
  delay(500);
  getComdata();
  if (comdata.length() > 0) {
    Serial.println(comdata);
    comdata = "";
  }
  Serial.println("GET http://pj.zhangtory.com/pj/record.php?t=" + String(temperature) + "&h=" + String(humidity) + "&pm=" + String(pm25) + "&lx=" + String(lx) + "&dev=" + devNum + " HTTP/1.0");
  wifi.println("GET http://pj.zhangtory.com/pj/record.php?t=" + String(temperature) + "&h=" + String(humidity) + "&pm=" + String(pm25) + "&lx=" + String(lx) + "&dev=" + devNum + " HTTP/1.0\r\n\r\n\r\n");
  wifi.println("");
  delay(10000);
  getComdata();
  if (!comdata.startsWith("HTTP")) {
    Serial.println(comdata);
    comdata = "";
    Serial.println("no back ,send again");
    wifi.println("GET http://pj.zhangtory.com/pj/record.php?t=" + String(temperature) + "&h=" + String(humidity) + "&pm=" + String(pm25) + "&lx=" + String(lx) + "&dev=" + devNum + " HTTP/1.0\r\n\r\n\r\n");
    wifi.println("");
    delay(6000);
    getComdata();
  }
  if (comdata.length() > 0) {
    Serial.println(comdata);
    comdata = "";
  }
  wifi.print("+++");
  delay(100);
  wifi.println("");
  delay(500);
  getComdata();
  if (comdata.length() > 0) {
    Serial.println(comdata);
    comdata = "";
  }
  wifi.println("AT+CIPCLOSE");//close TCP
  delay(2000);
  getComdata();
  if (comdata.length() > 0) {
    Serial.println(comdata);
    comdata = "";
  }
  wifi.println("AT+RST");
}

//Sleep mode is activated
void setup_watchdog() {
  byte bb;
  // 4 sec
  bb = 8 & 7;
  if (8 > 7) bb |= (1 << 5);
  bb |= (1 << WDCE);
  MCUSR &= ~(1 << WDRF);
  // start timed sequence
  WDTCSR |= (1 << WDCE) | (1 << WDE);
  // set new watchdog timeout value
  WDTCSR = bb;
  WDTCSR |= _BV(WDIE);
}

//WDT interrupt
ISR(WDT_vect) {
  ++sleepData;
  // wdt_reset();
}

void Sleep_avr() {
  set_sleep_mode(SLEEP_MODE_PWR_DOWN  ); // sleep mode is set here
  sleep_enable();
  sleep_mode();                        // System sleeps here
}
