package mv.mossuh.moarmors.DEBUG;

import mv.mossuh.moarmors.ENUMS.DebugType;

public class Debug {
    private DebugType debugType = DebugType.NONE;
    private boolean status = false;

    public Debug(DebugType debugType, Boolean status) {
        if (debugType != null) { this.debugType = debugType; }
        if (status != null) { this.status = status; }
    }

    public DebugType getDebugType() {
        return debugType;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        if (status != null) { this.status = status; }
    }
}
