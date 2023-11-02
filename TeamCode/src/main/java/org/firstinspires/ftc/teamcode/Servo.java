package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "ServoTeleOp", group = "TeleOp")
public class Servo extends OpMode {
    private CRServo servoTest;

    @Override
    public void init() {
        servoTest = hardwareMap.get(CRServo.class, "Servo"); // Use CRServo class for continuous rotation servos
    }

    @Override
    public void loop(){
        servoTest.setPower(0.5);
    }
}