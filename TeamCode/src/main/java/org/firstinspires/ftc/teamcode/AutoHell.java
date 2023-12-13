package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import java.util.ArrayList;
import java.util.List;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "AutoHell", group = "Combined")
public class AutoHell extends LinearOpMode {

    DcMotor Motor01, Motor02, Motor03, Motor04 = null;
    Servo Pixel_servo, Servo_arm1, Servo_arm2, Servo_hand1, Servo_hand2, Airplane = null;
    double frontPower, backPower, rightPower, leftPower;

    @Override
    public void runOpMode() {
        Motor01 = hardwareMap.get(DcMotor.class, "Motor01");
        Motor02 = hardwareMap.get(DcMotor.class, "Motor02");
        Motor03 = hardwareMap.get(DcMotor.class, "Motor03");
        Motor04 = hardwareMap.get(DcMotor.class, "Motor04");

        Motor01.setDirection(DcMotor.Direction.FORWARD);
        Motor02.setDirection(DcMotor.Direction.REVERSE);
        Motor03.setDirection(DcMotor.Direction.REVERSE);
        Motor04.setDirection(DcMotor.Direction.FORWARD);
        // Change this all to Run Without Encoder. Since we not gonna use it
        Motor01.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motor02.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor03.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motor04.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        DcMotor GearMotor = hardwareMap.get(DcMotor.class, "GearMotor");
        GearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Servo_arm1 = hardwareMap.get(Servo.class, "servo_arm1");
        Servo_arm2 = hardwareMap.get(Servo.class, "servo_arm2");
        Servo_hand1 = hardwareMap.get(Servo.class, "servo_hand1");
        Servo_hand2 = hardwareMap.get(Servo.class, "servo_hand2");
        Pixel_servo = hardwareMap.get(Servo.class, "pixel_servo");
        Airplane = hardwareMap.get(Servo.class, "air_servo");
        Servo_arm1.setDirection(Servo.Direction.REVERSE);
        Servo_hand1.setDirection(Servo.Direction.REVERSE);
        double MaxSpeed = 0;
        double x, y, rotate;
        double Desire_Range = 0;
        boolean TaskComplete = false;
        Pixel_servo.scaleRange(0, 0.8);
        Servo_arm1.scaleRange(0, 0.7);
        Servo_arm2.scaleRange(0, 0.7);
        Servo_hand1.scaleRange(0, 1);
        Servo_hand2.scaleRange(0, 1);

        while (opModeInInit()) {
            Servo_arm(1, true);
            Servo_hand(1, true);
            Pixel_servo.setPosition(0);
            sleep(1000);
            telemetry.update();
            Pixel_servo.setPosition(1);
            Servo_arm(0.95, true);
            Movement(1, 0, 0, 1);
            sleep(2000);
            Movement(0, -1, 0, 1);
            sleep(1500);
            Movement(1, 0, 0, 4);
            sleep(600);
            Prepare_drop();
            sleep(100);
            Drop_low1();
            sleep(400);
            Servo_arm(0.691, true);
            sleep(300);
            Prepare_drop();
            sleep(300);
            Movement(0, 1, 0, 4);
            sleep(400);
            Drop_low2();
            sleep(1000);
            Movement(-1, 0, 0, 1);
            sleep(400);
            Movement(0, 1, 0, 2);
            sleep(800);
            Movement(1, 0, 0, 2);
            sleep(800);

        }
    }




    public void Movement(double Frontway, double Sideway, double rotate, double slow){
        if (opModeIsActive()) {
            if (Frontway == 1) {
                backPower = -Frontway; //1
                frontPower = -Frontway; //0
                rightPower = Frontway; //3
                leftPower = Frontway; //2
            }

            if (Frontway == -1) {
                backPower = -Frontway;
                rightPower = Frontway;
                frontPower = -Frontway;
                leftPower = Frontway;
            }

            if (Sideway == 1) {
                backPower = Sideway;
                rightPower = Sideway;
                frontPower = Sideway;
                leftPower = Sideway;
            }

            if (Sideway == -1) {
                backPower = -Sideway;
                rightPower = -Sideway;
                frontPower = -Sideway;
                leftPower = -Sideway;
            }

            if (rotate == 1) {
                rightPower = -rotate;
                leftPower = rotate;
                frontPower = -rotate;
                backPower = rotate;
            } else if (rotate == -1) {
                rightPower = -rotate;
                leftPower = rotate;
                frontPower = -rotate;
                backPower = rotate;
            }

            Motor01.setPower(frontPower / slow);
            Motor02.setPower(backPower / slow);
            Motor03.setPower(leftPower / slow);
            Motor04.setPower(rightPower / slow);

            slow = 0;
        }
    }
    void Servo_arm(double Desire_Range, boolean Smooth)
    {
        while (opModeIsActive()){
            telemetry.addData("It works","");
            while (true) {
                if (Smooth && Servo_arm1.getPosition() < Desire_Range && Servo_arm2.getPosition() < Desire_Range) {
                    Servo_arm1.setPosition(Servo_arm1.getPosition() + 0.001);
                    Servo_arm1.setPosition(Servo_arm2.getPosition() + 0.001);
                }
                if (Smooth && Servo_arm1.getPosition() > Desire_Range && Servo_arm2.getPosition() > Desire_Range) {
                    Servo_arm1.setPosition(Servo_arm1.getPosition() - 0.001);
                    Servo_arm1.setPosition(Servo_arm1.getPosition() - 0.001);

                }

                if ((Servo_arm1.getPosition() == Desire_Range) || (Servo_arm2.getPosition() == Desire_Range)) {
                    break;

                }
            }
        }
    }
    void Servo_hand(double Desire_Range, boolean Smooth)
    {

        while (true) {

            if (Smooth && Servo_hand1.getPosition() < Desire_Range && Servo_hand2.getPosition() < Desire_Range) {
                Servo_hand1.setPosition(Servo_hand1.getPosition() + 0.001);
                Servo_hand2.setPosition(Servo_hand2.getPosition() + 0.001);
            }
            if (Smooth && Servo_hand1.getPosition() > Desire_Range && Servo_hand2.getPosition() > Desire_Range) {
                Servo_hand1.setPosition(Servo_hand1.getPosition() - 0.001);
                Servo_hand2.setPosition(Servo_hand1.getPosition() - 0.001);
            }

            if ((Servo_hand1.getPosition() == Desire_Range) || (Servo_hand2.getPosition() == Desire_Range)) {
                break;
            }

        }
    }

    void PickPixel()
    {
        Pixel_servo.setPosition(0);
        while (true) {
            Servo_arm1.setPosition(Servo_arm1.getPosition() + 0.001);
            Servo_arm2.setPosition(Servo_arm2.getPosition() + 0.001);

            if ((Servo_arm1.getPosition() == 1) || (Servo_arm2.getPosition() == 1)) {
                break;
            }
        }

        Servo_hand1.setPosition(0.45);
        Servo_hand2.setPosition(0.45);
        sleep(500);
        Pixel_servo.setPosition(1);

    }
    void Prepare_drop()
    {

        while (true) {
            if(Servo_arm1.getPosition() < 0.75) {
                Servo_arm1.setPosition(Servo_arm1.getPosition() + 0.001);
                Servo_arm2.setPosition(Servo_arm2.getPosition() + 0.001);
            }
            if(Servo_arm1.getPosition() > 0.75) {
                Servo_arm1.setPosition(Servo_arm1.getPosition() - 0.001);
                Servo_arm2.setPosition(Servo_arm2.getPosition() - 0.001);
            }
            if ((Servo_arm1.getPosition() == 0.75) || (Servo_arm2.getPosition() == 0.75)) {
                break;
            }

        }
    }
    void Drop_low1()
    {

        while (true) {
            Servo_hand1.setPosition(Servo_hand1.getPosition() + 0.001);
            Servo_hand2.setPosition(Servo_hand2.getPosition() + 0.001);
            if ((Servo_hand1.getPosition() == 0.24) || (Servo_hand2.getPosition() == 0.24)) {
                Pixel_servo.setPosition(0.3);
                break;
            }

        }
    }
    void Drop_low2()
    {

        while (true) {
            Servo_hand1.setPosition(Servo_hand1.getPosition() + 0.001);
            Servo_hand2.setPosition(Servo_hand2.getPosition() + 0.001);
            if ((Servo_hand1.getPosition() == 0.24) || (Servo_hand2.getPosition() == 0.24)) {
                Pixel_servo.setPosition(0);
                break;
            }
        }
    }


}