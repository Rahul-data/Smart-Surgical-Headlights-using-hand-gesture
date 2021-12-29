#include <Wire.h>
#include <ZX_Sensor.h>
// Constants
const int ZX_ADDR = 0x10; // ZX Sensor I2C address
// Global Variables
ZX_Sensor zx_sensor = ZX_Sensor(ZX_ADDR);
uint8_t x_pos;
uint8_t z_pos;
// This will store the most recent value of our swipe sensor.
int currentSensorValue1;
int currentSensorValue2;
// This stores the value we will compare with.
int previousSensorValue1 = 119;
int previousSensorValue2= 69;
int differenceValue1;
int differenceValue2;
/* Two independant timed evenets */
const unsigned long eventTime_1_LDR = 800; // interval in ms
//const unsigned long eventTime_2_temp = 5000;
unsigned long previousTime_1 = 0;
//unsigned long previousTime_2 = 0;
const int analogOutPin = 9; // Analog output pin that the LED is attached to
int sensorValue = 0; // value read from the pot
int outputValue = 0;
void setup() {
 
 uint8_t ver;
 pinMode( 9, OUTPUT);
62
 // Initialize Serial port
 Serial.begin(9600);
 Serial.println();
 Serial.println("-----------------------------------");
 Serial.println("SparkFun/GestureSense - I2C ZX Demo");
 Serial.println("-----------------------------------");
 
 // Initialize ZX Sensor (configure I2C and read model ID)
 if ( zx_sensor.init() ) {
 Serial.println("ZX Sensor initialization complete");
 } else {
 Serial.println("Something went wrong during ZX Sensor init!");
 }
 
 // Read the model version number and ensure the library will work
 ver = zx_sensor.getModelVersion();
 if ( ver == ZX_ERROR ) {
 Serial.println("Error reading model version number");
 } else {
 Serial.print("Model version: ");
 Serial.println(ver);
 }
 if ( ver != ZX_MODEL_VER ) {
 Serial.print("Model version needs to be ");
 Serial.print(ZX_MODEL_VER);
 Serial.print(" to work with this library. Stopping.");
 while(1);
 }
 
 // Read the register map version and ensure the library will work
 ver = zx_sensor.getRegMapVersion();
 if ( ver == ZX_ERROR ) {
 Serial.println("Error reading register map version number");
 } else {
 Serial.print("Register Map Version: ");
 Serial.println(ver);
 }
 if ( ver != ZX_REG_MAP_VER ) {
 Serial.print("Register map version needs to be ");
 Serial.print(ZX_REG_MAP_VER);
 Serial.print(" to work with this library. Stopping.");
 while(1);
 }
}
63
void loop() {
 
 // If there is position data available, read and print it
 unsigned long currentTime = millis();
 //sensorValue = analogRead(analogInPin);
 if ( zx_sensor.positionAvailable() ) {
 x_pos = zx_sensor.readX();
 if ( x_pos != ZX_ERROR ) {
 Serial.print("X: ");
 Serial.print(x_pos);
 }
 z_pos = zx_sensor.readZ();
 if ( z_pos != ZX_ERROR ) {
 Serial.print(" Z: ");
 Serial.println(z_pos);
 }
 }
 currentSensorValue1 = x_pos;
 currentSensorValue2 = z_pos;
 //prevoiusSensorValue = 120;
 differenceValue1 = (currentSensorValue1 - previousSensorValue1);
 differenceValue2 = (currentSensorValue2 - previousSensorValue2);
 if(differenceValue1 >= 6)
 {
 analogWrite(9, 250);
// digitalWrite(9, HIGH);
// Serial.println("Right_swipe");
 delay(1500);
 // previousSensorValue1 = currentSensorValue1;
 } else if(differenceValue1 <= -4){
 
 analogWrite(9,50);
// digitalWrite(9, LOW);
// Serial.println("Left Swipe");
 //previousSensorValue1 = currentSensorValue1;
 delay(1800);
 } else{
 //Serial.println("No swipe");
 
 }
 if(differenceValue2 >= 50)
 {
 digitalWrite(9, HIGH);
 Serial.println("Right_swipe");
64
// analogWrite(9,250);
// Serial.println("Medium_light");
 delay(1500);
 //previousSensorValue1 = currentSensorValue1;
 } else if(differenceValue2 <= -40){
 digitalWrite(9, LOW);
 Serial.println("Left Swipe");
// analogWrite(9, 50);
 delay(1500);
// Serial.println("Low_light");
 // previousSensorValue1 = currentSensorValue1;
 } else{
 //analogWrite(9,250);
 //Serial.println("No swipe");
 
 }
 
 
 if( currentTime - previousTime_1 >= eventTime_1_LDR ){
 previousSensorValue1 = currentSensorValue1;
 previousSensorValue2 = currentSensorValue2;
 //Serial.print ("LDR: ");
 //Serial.println( analogRead(LDR) );
 /* Update the timing for the next event */
 previousTime_1 = currentTime;
//delay(500);
}
}