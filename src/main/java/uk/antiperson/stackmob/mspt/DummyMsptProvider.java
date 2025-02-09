package uk.antiperson.stackmob.mspt;

public class DummyMsptProvider extends MsptProvider {

    @Override
    public double getMspt() {
        return 0;
    }

    @Override
    public void setUnderLoad(boolean underLoad) { }

    @Override
    public boolean isUnderLoad() {
        return false;
    }
}
