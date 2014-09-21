// Controlling a servo position using a potentiometer (variable resistor) 
// by Michal Rinott <http://people.interaction-ivrea.it/m.rinott> 

#include <Servo.h> 
#include <MeetAndroid.h>
 
Servo myservo;  // create servo object to control a servo 
MeetAndroid meetAndroid;
 
int potpin = 0;  // analog pin used to connect the potentiometer
int val;    // variable to read the value from the analog pin 
int zval=10;
int LEDpin = 13;
 
void setup() 
{ 
  Serial.begin(9600);
  pinMode(LEDpin, OUTPUT); 
  myservo.attach(9);  // attaches the servo on pin 9 to the servo object 
  meetAndroid.registerFunction(setz, 'A');
} 
 
void loop() 
{ 
  meetAndroid.receive();
  //val = analogRead(potpin);            // reads the value of the potentiometer (value between 0 and 1023) 
  if(zval > 0)
    digitalWrite(LEDpin, HIGH); 
  else
    digitalWrite(LEDpin, LOW);  
   
  Serial.println(zval);  
  val = map(zval, -100, 100, 0, 179);     // scale it to use it with the servo (value between 0 and 180) 
  myservo.write(val);                  // sets the servo position according to the scaled value 
  delay(15);                           // waits for the servo to get there 
} 

void setz(byte flag, byte numOfValues)
{
  zval = meetAndroid.getInt(); 
}
