package github.leavesc.wifip2p.model;

import java.io.Serializable;

public class TransferBody implements Serializable {

    private int code;
    private FileTransfer fileTransfer;
    private int dx;

    public TransferBody(int code, FileTransfer fileTransfer, int dx) {
        this.code = code;
        this.fileTransfer = fileTransfer;
        this.dx = dx;
    }

    public FileTransfer getFileTransfer() {
        return fileTransfer;
    }

    public int getCode() {
        return code;
    }

    public int getDx() {
        return dx;
    }
}
