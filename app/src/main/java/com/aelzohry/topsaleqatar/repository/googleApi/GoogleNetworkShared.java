package com.aelzohry.topsaleqatar.repository.googleApi;


public class GoogleNetworkShared {

    private GoogleNetworkShared() {
    }

    private static ASP asp;

    public static ASP getAsp() {
        if (asp == null)
            asp = new ASP();
        return asp;
    }

    public static class ASP {

        private General mGeneral;

        private ASP() {
            mGeneral = new General();

        }


        public General getGeneral() {
            return mGeneral;
        }

    }
}
