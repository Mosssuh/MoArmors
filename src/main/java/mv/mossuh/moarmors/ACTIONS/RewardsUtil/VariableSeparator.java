package mv.mossuh.moarmors.ACTIONS.RewardsUtil;

import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;

public class VariableSeparator {

    private String string = "";
    private boolean value = false;
    private PieceType pieceType = PieceType.NONE;
    private VariableArg variable = new VariableArg(null, null);

    public VariableSeparator(String string, Boolean value) {
        if (string != null) { this.string = string; }
        if (value != null) { this.value = value; }
        if (this.value) {
            // SET_VARIABLE -> a_variable::test
            splitVariable();
        } else {
            // REMOVE_VARIABLE -> a_variable
            splitVariableNoValue();
        }
    }

    public PieceType getPieceType() { return pieceType; }
    public VariableArg getVariable() { return variable; }

    private void splitVariable() {
        String[] stringSplit = this.string.split("::", 2);
        if (stringSplit.length == 2) {
            String variable = stringSplit[0];
            String value = stringSplit[1];
            this.variable = new VariableArg(variable, value);
        }
    }

    private void splitVariableNoValue() {
        this.variable = new VariableArg(this.string, null);
    }
}
