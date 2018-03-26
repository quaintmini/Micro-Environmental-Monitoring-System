#include <SoftwareSerial.h>

SoftwareSerial wifi(10, 11); // RX, TX
String comdata = ""; //get from wifi rx,tx

void setup() {
  Serial.begin(2400);
  Serial.println("Serial Connected!");
  wifi.begin(9600);
}

//read wifi rx,tx
void getComdata()
{
   while (wifi.available() > 0) {
     comdata += char(wifi.read());
     delay(2);
    }
}

void loop() {
  getComdata();
  if (comdata.length() > 0) {
    Serial.println(comdata);
    comdata = "";
  }
  if (Serial.available()) {
    wifi.write(Serial.read());
  }
}
