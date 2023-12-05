package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

import java.util.List;

@Autonomous(name = "AutoHell", group = "Combined")
public class AutoHell extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;

    private TfodProcessor tfod;
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {

//        initTfod();

        DcMotorEx Motor01 = hardwareMap.get(DcMotorEx.class, "Motor01");
        DcMotorEx Motor02 = hardwareMap.get(DcMotorEx.class, "Motor02");
        DcMotorEx Motor03 = hardwareMap.get(DcMotorEx.class, "Motor03");
        DcMotorEx Motor04 = hardwareMap.get(DcMotorEx.class, "Motor04");

        Motor01.setDirection(DcMotorEx.Direction.FORWARD);
        Motor02.setDirection(DcMotorEx.Direction.REVERSE);
        Motor03.setDirection(DcMotorEx.Direction.REVERSE);
        Motor04.setDirection(DcMotorEx.Direction.FORWARD);

        Motor01.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        Motor02.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        Motor03.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        Motor04.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        Servo Servo_arm1 = hardwareMap.get(Servo.class, "servo_arm1");
        Servo Servo_arm2 = hardwareMap.get(Servo.class, "servo_arm2");
        Servo Servo_hand1 = hardwareMap.get(Servo.class, "servo_hand1");
        Servo Servo_hand2 = hardwareMap.get(Servo.class, "servo_hand2");
        Servo Pixel_servo = hardwareMap.get(Servo.class, "pixel_servo");
        Servo Airplane = hardwareMap.get(Servo.class, "air_servo");

        double velocity_still = 0;
        double velocity_forward = 1400;
        double velocity_backward = -1400;
        double brake = 0;
        double mappedServoPosition;
        double velocity = 1400;
        double still = 0;
        int[] MMotor1 = new int[30];
        int[] MMotor2 = new int[30];
        int[] MMotor3 = new int[30];
        int[] MMotor4 = new int[30];

        int i = 0;
        //เรียกใช้
        int M1 = MMotor1[i];
        int M2 = MMotor1[i];
        int M3 = MMotor1[i];
        int M4 = MMotor1[i];
        double x,y,rotate;
        boolean trigger_left;
        boolean trigger_right;
        double range_motor_arm = 0;
        double range_motor_hand = 0;
        boolean Isit_1 = true;
        boolean isMovingToPosition1 = false;
        Motor01.getCurrentPosition();
        Motor03.getCurrentPosition();
        telemetry.addData("Starting at",  "%7d :%7d",
                Motor01.getCurrentPosition(),
                Motor03.getCurrentPosition());

        //telemetry.addData("Going to...", "%7d :%7d"),)
        telemetry.update();



        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                MMotor1[i] = M1;
                MMotor2[i] = M2;
                MMotor3[i] = M3;
                MMotor4[i] = M4;

                if (gamepad1.y)
                    while(true) {
                        { //mapping
                            x = gamepad1.left_stick_x;
                            y = gamepad1.left_stick_y;
                            rotate = gamepad1.right_stick_x;


                            if (gamepad1.right_bumper) {
                                velocity = 470;
                            }

                            if (gamepad1.left_bumper) {
                                velocity = 1400;
                            }
//                    Motor01.setTargetPosition();
//                    Motor02.setTargetPosition();

                            if (x != 1 && y != 1) {
                                Motor01.setVelocity(still);
                                Motor02.setVelocity(still);
                                Motor03.setVelocity(still);
                                Motor04.setVelocity(still);
                            } else if (x != -1 && y != -1) {
                                Motor01.setVelocity(still);
                                Motor02.setVelocity(still);
                                Motor03.setVelocity(still);
                                Motor04.setVelocity(still);
                            }

                            if (y == 1) {
                                Motor01.setVelocity(-velocity);
                                Motor02.setVelocity(-velocity);
                                Motor03.setVelocity(velocity);
                                Motor04.setVelocity(velocity);
                            } else if (y == -1) {
                                Motor01.setVelocity(velocity);
                                Motor02.setVelocity(velocity);
                                Motor03.setVelocity(-velocity);
                                Motor04.setVelocity(-velocity);
                            }

                            if (x == 1) {
                                Motor01.setVelocity(velocity);
                                Motor02.setVelocity(velocity);
                                Motor03.setVelocity(velocity);
                                Motor04.setVelocity(velocity);
                            } else if (x == -1) {
                                Motor01.setVelocity(-velocity);
                                Motor02.setVelocity(-velocity);
                                Motor03.setVelocity(-velocity);
                                Motor04.setVelocity(-velocity);
                            }

                            if (rotate == 1) {
                                Motor01.setVelocity(-velocity);
                                Motor02.setVelocity(velocity);
                                Motor03.setVelocity(velocity);
                                Motor04.setVelocity(-velocity);
                            } else if (rotate == -1) {
                                Motor01.setVelocity(velocity);
                                Motor02.setVelocity(-velocity);
                                Motor03.setVelocity(-velocity);
                                Motor04.setVelocity(velocity);
                            }
                            if (gamepad1.x) {

                                M1 = Motor01.getCurrentPosition();
                                M2 = Motor02.getCurrentPosition();
                                M3 = Motor03.getCurrentPosition();
                                M4 = Motor04.getCurrentPosition();
                                i += 1;
                                telemetry.addData("State = ", i);
                                telemetry.update();
                            }
                            if (gamepad1.a)
                            {
                                telemetry.addData("Autonomous Activated ..", "");
                                break;
                            }
                        }
                    }
                //main autonomous
                Motor01.setTargetPosition(M1);
                Motor02.setTargetPosition(M2);
                Motor03.setTargetPosition(M3);
                Motor04.setTargetPosition(M4);
            }



        }

    /*private void initTfod() {
        tfod = TfodProcessor.easyCreateWithDefaults();

        if (USE_WEBCAM) {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    hardwareMap.get(WebcamName.class, "webcam"), tfod);
        } else {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    BuiltinCameraDirection.BACK, tfod);
        }
    }

    private void telemetryTfod() {
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }*/
    }
}

