package tv.zimuzu.sdk.p4pclient;

import java.io.Serializable;
import java.util.List;

public class P4PStat implements Serializable {
    public static final int P4PSTATE_DLING = 1;
    public static final int P4PSTATE_DLOK = 2;
    public static final int P4PSTATE_ERR = 4;
    public static final int P4PSTATE_IDLE = 0;
    public static final int P4PSTATE_STOPPING = 3;
    private int downSpeed;
    private List<P4PTaskStat> stats;
    private int upSpeed;

    public String string() {
        return "P4PStat{" +
                "downSpeed=" + downSpeed +
                ", stats=" + stats +
                ", upSpeed=" + upSpeed +
                '}';
    }

    public class P4PTaskStat implements Serializable {
        private int downSpeed;
        private int errCode;
        private long fileSize;
        private long finishedSize;
        private String id;
        private int state;
        private int upSpeed;

        @Override
        public String toString() {
            return "P4PTaskStat{\n" +
                    "downSpeed=" + downSpeed +
                    ",\n errCode=" + errCode +
                    ",\n fileSize=" + fileSize +
                    ",\n finishedSize=" + finishedSize +
                    ",\n id='" + id + '\'' +
                    ",\n state=" + state +
                    ",\n upSpeed=" + upSpeed +
                    "\n}";
        }

        public P4PTaskStat() {
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public int getState() {
            return this.state;
        }

        public void setState(int state2) {
            this.state = state2;
        }

        public int getDownSpeed() {
            return this.downSpeed;
        }

        public void setDownSpeed(int speed) {
            this.downSpeed = speed;
        }

        public int getUpSpeed() {
            return this.upSpeed;
        }

        public void setUpSpeed(int speed) {
            this.upSpeed = speed;
        }

        public long getFileSize() {
            return this.fileSize;
        }

        public void setFileSize(long fileSize2) {
            this.fileSize = fileSize2;
        }

        public long getFinishedSize() {
            return this.finishedSize;
        }

        public void setFinishedSize(int finishedSize2) {
            this.finishedSize = (long) finishedSize2;
        }

        public void setErrCode(int errCode2) {
            this.errCode = errCode2;
        }

        public int getErrCode() {
            return this.errCode;
        }
    }

    public int getDownSpeed() {
        return this.downSpeed;
    }

    public void setDownSpeed(int speed) {
        this.downSpeed = speed;
    }

    public int getUpSpeed() {
        return this.upSpeed;
    }

    public void setUpSpeed(int speed) {
        this.upSpeed = speed;
    }

    public List<P4PTaskStat> getStats() {
        return this.stats;
    }

    public void setStats(List<P4PTaskStat> stats2) {
        this.stats = stats2;
    }
}
