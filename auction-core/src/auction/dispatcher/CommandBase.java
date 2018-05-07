package auction.dispatcher;

import auction.commands.Command;

import java.util.Date;

public class CommandBase {

    private Date timestamp;
    private Command command;
    private String rawMessage;

    public CommandBase() {

    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }
}
