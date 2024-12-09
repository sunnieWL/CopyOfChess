package game;

public class Timer {
    private int hours;
    private int minutes;
    private int seconds;
    private boolean isRunning;

    public Timer(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.hours = 0;
        this.isRunning = false;
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public void decrementTimer(int secondsToDecrement) {
        if (!isRunning) return;

        int totalSeconds = (hours * 3600) + (minutes * 60) + seconds - secondsToDecrement;
        if (totalSeconds <= 0) {
            this.hours = 0;
            this.minutes = 0;
            this.seconds = 0;
        } else {
            this.hours = totalSeconds / 3600;
            this.minutes = (totalSeconds % 3600) / 60;
            this.seconds = totalSeconds % 60;
        }
    }

    public boolean isTimerEmpty() {
        return hours == 0 && minutes == 0 && seconds == 0;
    }

    public String getTime() {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public boolean isRunning() {
        return isRunning;
    }
}

