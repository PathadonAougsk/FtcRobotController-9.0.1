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

        initTfod();

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

        Servo servo_left = hardwareMap.get(Servo.class, "Servo_left");
        Servo servo_right = hardwareMap.get(Servo.class, "Servo_right");

        double velocity_still = 0;
        double velocity_forward = 1400;
        double velocity_backward = -1400;
        double brake = 0;

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
//                Motor01.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                Motor02.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                Motor03.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                Motor04.setMode(DcMotor.RunMode.RUN_TO_POSITION);
               
                if (gamepad1.a) {
                    telemetry.addData("Starting at",  "%7d :%7d",
                            Motor01.getCurrentPosition(),
                            Motor02.getCurrentPosition(),
                            Motor03.getCurrentPosition(),
                            Motor04.getCurrentPosition());
                }

                if (gamepad1.b){
                    // Maybe we can add some way to safe this data if that necessary
                    // Track movement?
                    // when certain time hit?
                }


            }
        }

        // Save more CPU resources when the camera is no longer needed.
        visionPortal.close();
    }

    private void initTfod() {
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
        }
    }
}

