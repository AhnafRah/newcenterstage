package org.firstinspires.ftc.teamcode.Drive.OpModes.States.BasicAutonomous50;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Drive.DriveConstants;
import org.firstinspires.ftc.teamcode.Drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.TrajectorySequences.TrajectorySequence;
import org.firstinspires.ftc.teamcode.Utility.CommandBase.Commands.BlueStackCommand;
import org.firstinspires.ftc.teamcode.Utility.CommandBase.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.Utility.CommandBase.Commands.OuttakeCommand;
import org.firstinspires.ftc.teamcode.Utility.CommandBase.Commands.SlidesDownCommand;
import org.firstinspires.ftc.teamcode.Utility.CommandBase.Commands.SuperHighOuttakeCommand;
import org.firstinspires.ftc.teamcode.Utility.CommandBase.Commands.TwoPixelDropCommand;
import org.firstinspires.ftc.teamcode.Utility.CommandBase.Commands.TwoPixelDropRightCommand;
import org.firstinspires.ftc.teamcode.Utility.Hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.Utility.Vision.Prop.NewRedLeftProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous
public class SixtyRedRight extends OpMode {
    private VisionPortal visionPortal;
    private NewRedLeftProcessor colorMassDetectionProcessor;

    private RobotHardware robot;
    private ElapsedTime time_since_start;
    private double loop;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robot = new RobotHardware(hardwareMap);

        CommandScheduler.getInstance().registerSubsystem(robot.armSystem);
        CommandScheduler.getInstance().registerSubsystem(robot.claw);
        CommandScheduler.getInstance().registerSubsystem(robot.angleOfArm);
        CommandScheduler.getInstance().registerSubsystem(robot.driveSubsystem);

        telemetry.addData("Not Ready: ", "Not able to proceed to camera detection... Restart robot now.");
        telemetry.update();

        robot.claw.grabBoth();

        colorMassDetectionProcessor = new NewRedLeftProcessor();
        colorMassDetectionProcessor.setDetectionColor(false); //false is blue, true is red

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam"))
                .addProcessor(colorMassDetectionProcessor)
                .build();
    }

    @Override
    public void init_loop() {
        telemetry.addData("Currently Recorded Position: ", colorMassDetectionProcessor.getPropLocation());
        telemetry.addData("Camera State: ", visionPortal.getCameraState());
        CommandScheduler.getInstance().run();
        robot.armSystem.loop();
    }

    @Override
    public void start() {
        time_since_start = new ElapsedTime();
        if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
            visionPortal.stopLiveView();
            visionPortal.stopStreaming();
        }

        NewRedLeftProcessor.PropPositions recordedPropPosition = colorMassDetectionProcessor.getPropLocation();
        robot.driveSubsystem.setPoseEstimate(new Pose2d(16.14, 63.32, Math.toRadians(90.00)));
        switch (recordedPropPosition) {
            case LEFT:
                TrajectorySequence movement1Left = robot.driveSubsystem.trajectorySequenceBuilder(new Pose2d(16.14, -63.32, Math.toRadians(90.00)))
                        .splineTo(
                                new Vector2d(11, -35.75), Math.toRadians(140.00),
                                SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .lineToSplineHeading(
                                new Pose2d(19.31, -46.84, Math.toRadians(90.00)),
                                SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();

                TrajectorySequence movement2Left = robot.driveSubsystem.trajectorySequenceBuilder(movement1Left.end())
                        .splineToSplineHeading(
                                new Pose2d(53.80, -26, Math.toRadians(360.00)), Math.toRadians(360.00),
                                SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();

                TrajectorySequence movement3Left = robot.driveSubsystem.trajectorySequenceBuilder(movement2Left.end())
                        .lineToConstantHeading(
                                new Vector2d(46, -40.5),
                                SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .lineToConstantHeading(
                                new Vector2d(25, -13),
                                SampleMecanumDrive.getVelocityConstraint(27, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();

                TrajectorySequence movement4Left = robot.driveSubsystem.trajectorySequenceBuilder(movement3Left.end())
                        .lineToLinearHeading(
                                new Pose2d(-58.2, -13.45, Math.toRadians(360)),
                                SampleMecanumDrive.getVelocityConstraint(29, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )

                        .lineToLinearHeading(
                                new Pose2d(-58.5, -13.5, Math.toRadians(359.07)),
                                SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )


                        .build();
                TrajectorySequence movement5Left = robot.driveSubsystem.trajectorySequenceBuilder(movement4Left.end())
                        .lineToConstantHeading(
                                new Vector2d(25, -13.5),
                                SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();
                TrajectorySequence movement6Left = robot.driveSubsystem.trajectorySequenceBuilder(movement5Left.end())
                        .lineToConstantHeading(
                                new Vector2d(47.9, -28.8),
                                SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();
                TrajectorySequence movement7Left = robot.driveSubsystem.trajectorySequenceBuilder(movement6Left.end())
                        .lineToConstantHeading(
                                new Vector2d(46, -27),
                                SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .lineToConstantHeading(
                                new Vector2d(42.3, -26),
                                SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();


                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new DriveCommand(robot.driveSubsystem, movement1Left),
                                new ParallelCommandGroup(
                                        new DriveCommand(robot.driveSubsystem, movement2Left),
                                        new OuttakeCommand(robot)
                                ),
                                new WaitCommand(400),
                                new InstantCommand(() -> robot.claw.releaseRight()),
                                new WaitCommand(400),
                                new ParallelCommandGroup(
                                        new DriveCommand(robot.driveSubsystem, movement3Left),
                                        new WaitCommand(200),
                                        new BlueStackCommand(robot)
                                ),
                                new InstantCommand(() -> robot.claw.customRight(1)),
                                new DriveCommand(robot.driveSubsystem, movement4Left),
                                new WaitCommand(300),
                                new InstantCommand(() -> robot.claw.grabBoth()),
                                new DriveCommand(robot.driveSubsystem, movement5Left),
                                new ParallelCommandGroup(

                                        new DriveCommand(robot.driveSubsystem, movement6Left),
                                        new SuperHighOuttakeCommand(robot)
                                ),

                                new TwoPixelDropRightCommand(robot),
                                new WaitCommand(1000),
                                new InstantCommand(() -> robot.claw.customRight(0.7)),
                                new WaitCommand(300),
                                new DriveCommand(robot.driveSubsystem, movement7Left),
                                new SlidesDownCommand(robot)



                        )
                );
                break;
            case RIGHT:
            case UNFOUND:
                TrajectorySequence movement1Right = robot.driveSubsystem.trajectorySequenceBuilder(new Pose2d(16.14, -63.32, Math.toRadians(90.00)))
                        .splineToConstantHeading(
                                new Vector2d(27.80, -43.31), Math.toRadians(90.00),
                                SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .lineToConstantHeading(
                                new Vector2d(27.80, -56.52),
                                SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();

                TrajectorySequence movement2Right = robot.driveSubsystem.trajectorySequenceBuilder(movement1Right.end())
                        .splineToSplineHeading(
                                new Pose2d(54.2, -39, Math.toRadians(0.00)), Math.toRadians(0.00),
                                SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();

                TrajectorySequence movement3Right = robot.driveSubsystem.trajectorySequenceBuilder(movement2Right.end())
                        .lineToConstantHeading(
                                new Vector2d(46, -28),
                                SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .lineToConstantHeading(
                                new Vector2d(25, -13),
                                SampleMecanumDrive.getVelocityConstraint(27, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();

                TrajectorySequence movement4Right = robot.driveSubsystem.trajectorySequenceBuilder(movement3Right.end())
                        .lineToLinearHeading(
                                new Pose2d(-58.2, -13.45, Math.toRadians(360)),
                                SampleMecanumDrive.getVelocityConstraint(29, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )

                        .lineToLinearHeading(
                                new Pose2d(-58.5, -13.5, Math.toRadians(359.07)),
                                SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )


                        .build();
                TrajectorySequence movement5Right = robot.driveSubsystem.trajectorySequenceBuilder(movement4Right.end())
                        .lineToConstantHeading(
                                new Vector2d(25, -13.5),
                                SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();
                TrajectorySequence movement6Right = robot.driveSubsystem.trajectorySequenceBuilder(movement5Right.end())
                        .lineToConstantHeading(
                                new Vector2d(47.9, -28.8),
                                SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();
                TrajectorySequence movement7Right = robot.driveSubsystem.trajectorySequenceBuilder(movement6Right.end())
                        .lineToConstantHeading(
                                new Vector2d(46, -27),
                                SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .lineToConstantHeading(
                                new Vector2d(42.3, -26),
                                SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();


                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new DriveCommand(robot.driveSubsystem, movement1Right),
                                new ParallelCommandGroup(
                                        new DriveCommand(robot.driveSubsystem, movement2Right),
                                        new OuttakeCommand(robot)
                                ),
                                new WaitCommand(400),
                                new InstantCommand(() -> robot.claw.releaseRight()),
                                new WaitCommand(400),
                                new ParallelCommandGroup(
                                        new DriveCommand(robot.driveSubsystem, movement3Right),
                                        new WaitCommand(200),
                                        new BlueStackCommand(robot)
                                ),
                                new InstantCommand(() -> robot.claw.customRight(1)),
                                new DriveCommand(robot.driveSubsystem, movement4Right),
                                new WaitCommand(300),
                                new InstantCommand(() -> robot.claw.grabBoth()),
                                new DriveCommand(robot.driveSubsystem, movement5Right),
                                new ParallelCommandGroup(

                                        new DriveCommand(robot.driveSubsystem, movement6Right),
                                        new SuperHighOuttakeCommand(robot)
                                ),

                                new TwoPixelDropRightCommand(robot),
                                new WaitCommand(1000),
                                new InstantCommand(() -> robot.claw.customRight(0.7)),
                                new WaitCommand(300),
                                new DriveCommand(robot.driveSubsystem, movement7Right),
                                new SlidesDownCommand(robot)



                        )
                );
                break;
            case MIDDLE:
                TrajectorySequence movement1Middle = robot.driveSubsystem.trajectorySequenceBuilder(new Pose2d(16.14, -63.32, Math.toRadians(450.00)))
                        .splineToConstantHeading(
                                new Vector2d(20.25, -33.88), Math.toRadians(90.00),
                                SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .lineToConstantHeading(
                                new Vector2d(20.25, -50.07),
                                SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();

                TrajectorySequence movement2Middle = robot.driveSubsystem.trajectorySequenceBuilder(movement1Middle.end())
                        .splineToSplineHeading(
                                new Pose2d(54.2, -32.40, Math.toRadians(360.00)), Math.toRadians(360.00),
                                SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();

                TrajectorySequence movement3Middle = robot.driveSubsystem.trajectorySequenceBuilder(movement2Middle.end())
                        .lineToConstantHeading(
                                new Vector2d(46, -32),
                                SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .lineToConstantHeading(
                                new Vector2d(25, -13),
                                SampleMecanumDrive.getVelocityConstraint(27, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();

                TrajectorySequence movement4Middle = robot.driveSubsystem.trajectorySequenceBuilder(movement3Middle.end())
                        .lineToLinearHeading(
                                new Pose2d(-58.2, -13.45, Math.toRadians(360)),
                                SampleMecanumDrive.getVelocityConstraint(29, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )

                        .lineToLinearHeading(
                                new Pose2d(-58.5, -13.5, Math.toRadians(359.07)),
                                SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )


                        .build();
                TrajectorySequence movement5Middle = robot.driveSubsystem.trajectorySequenceBuilder(movement4Middle.end())
                        .lineToConstantHeading(
                                new Vector2d(25, -13.5),
                                SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();
                TrajectorySequence movement6Middle = robot.driveSubsystem.trajectorySequenceBuilder(movement5Middle.end())
                        .lineToConstantHeading(
                                new Vector2d(47.9, -28.8),
                                SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();
                TrajectorySequence movement7Middle = robot.driveSubsystem.trajectorySequenceBuilder(movement6Middle.end())
                        .lineToConstantHeading(
                                new Vector2d(46, -27),
                                SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .lineToConstantHeading(
                                new Vector2d(42.3, -26),
                                SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build();


                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new DriveCommand(robot.driveSubsystem, movement1Middle),
                                new ParallelCommandGroup(
                                        new DriveCommand(robot.driveSubsystem, movement2Middle),
                                        new OuttakeCommand(robot)
                                ),
                                new WaitCommand(400),
                                new InstantCommand(() -> robot.claw.releaseRight()),
                                new WaitCommand(400),
                                new ParallelCommandGroup(
                                        new DriveCommand(robot.driveSubsystem, movement3Middle),
                                        new WaitCommand(200),
                                        new BlueStackCommand(robot)
                                ),
                                new InstantCommand(() -> robot.claw.customRight(1)),
                                new DriveCommand(robot.driveSubsystem, movement4Middle),
                                new WaitCommand(300),
                                new InstantCommand(() -> robot.claw.grabBoth()),
                                new DriveCommand(robot.driveSubsystem, movement5Middle),
                                new ParallelCommandGroup(

                                        new DriveCommand(robot.driveSubsystem, movement6Middle),
                                        new SuperHighOuttakeCommand(robot)
                                ),

                                new TwoPixelDropRightCommand(robot)),
                                new WaitCommand(1000),
                                new InstantCommand(() -> robot.claw.customRight(0.7)),
                                new WaitCommand(300),
                                new DriveCommand(robot.driveSubsystem, movement7Middle),
                                new SlidesDownCommand(robot)




                );
                break;
        }
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        robot.armSystem.loop();
        robot.slidesSubsystem.loop();
        robot.driveSubsystem.update();

        double time = System.currentTimeMillis();
        telemetry.addData("Time Elapsed: ", time_since_start);
        telemetry.addData("Current Loop Time: ", time - loop);

        loop = time;
        telemetry.update();
    }

    @Override
    public void stop() {
        visionPortal.close();
        telemetry.addLine("Closed Camera.");
        telemetry.update();
        CommandScheduler.getInstance().reset();
    }
}