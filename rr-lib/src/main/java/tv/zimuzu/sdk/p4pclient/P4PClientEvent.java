package tv.zimuzu.sdk.p4pclient;

public interface P4PClientEvent {
    void onP4PClientInited();

    void onP4PClientRestarted();

    void onP4PClientStarted();

    void onTaskStat(P4PStat p4PStat);
}
