package com.fronttcapital.aliossimageloader.mvp.contract;

public interface IMainActivityContract {
    interface View {
        void getOSSAccessKeySucceed();

        void getOSSAccessKeyFailed(String msg);
    }

    interface Presenter {
        void getOSSAccessKey();
    }
}
