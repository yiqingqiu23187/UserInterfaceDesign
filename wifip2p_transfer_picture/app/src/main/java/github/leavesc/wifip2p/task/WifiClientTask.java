package github.leavesc.wifip2p.task;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

import github.leavesc.wifip2p.SendFileActivity;
import github.leavesc.wifip2p.common.Constants;
import github.leavesc.wifip2p.model.FileTransfer;
import github.leavesc.wifip2p.model.TransferBody;
import github.leavesc.wifip2p.util.Md5Util;


public class WifiClientTask extends AsyncTask<Object, Integer, Boolean> {

    private static final String TAG = "WifiClientTask";

    @SuppressLint("StaticFieldLeak")
    private final Context context;

    public WifiClientTask(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
    }

    private String getOutputFilePath(Uri fileUri) throws Exception {
        String outputFilePath = context.getExternalCacheDir().getAbsolutePath() +
                File.separatorChar + new Random().nextInt(10000) +
                new Random().nextInt(10000) + ".jpg";
        File outputFile = new File(outputFilePath);
        if (!outputFile.exists()) {
            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();
        }
        Uri outputFileUri = Uri.fromFile(outputFile);
        copyFile(context, fileUri, outputFileUri);
        return outputFilePath;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        Socket socket = null;
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        InputStream inputStream = null;
        try {
            String hostAddress = params[0].toString();
            int code = Integer.parseInt(params[2].toString());

            socket = new Socket();
            socket.bind(null);
            socket.connect((new InetSocketAddress(hostAddress, Constants.PORT)), 10000);
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);

            if (code == 1)
                fileInBackground(Uri.parse(params[1].toString()),outputStream,objectOutputStream,inputStream);
            else
                DxInBackground(Integer.parseInt(params[1].toString()),outputStream,objectOutputStream);

            socket.close();
            outputStream.close();
            objectOutputStream.close();
            socket = null;
            inputStream = null;
            outputStream = null;
            objectOutputStream = null;
            return true;
        } catch (Exception e) {
            Log.e(TAG, "文件发送异常 Exception: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void fileInBackground(Uri imageUri, OutputStream outputStream, ObjectOutputStream objectOutputStream, InputStream inputStream) throws Exception{
        String outputFilePath = getOutputFilePath(imageUri);
        File outputFile = new File(outputFilePath);

        FileTransfer fileTransfer = new FileTransfer();
        String fileName = outputFile.getName();
        String fileMa5 = Md5Util.getMd5(outputFile);
        long fileLength = outputFile.length();
        fileTransfer.setFileName(fileName);
        fileTransfer.setMd5(fileMa5);
        fileTransfer.setFileLength(fileLength);

        TransferBody transferBody = new TransferBody(1, fileTransfer, 0);

        objectOutputStream.writeObject(transferBody);
        inputStream = new FileInputStream(outputFile);
        long fileSize = fileTransfer.getFileLength();
        long total = 0;
        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
            total += len;
            int progress = (int) ((total * 100) / fileSize);
            publishProgress(progress);
        }
        inputStream.close();
    }

    private void DxInBackground(Integer dx, OutputStream outputStream, ObjectOutputStream objectOutputStream) throws Exception {
        TransferBody transferBody = new TransferBody(2, null, dx);
        objectOutputStream.writeObject(transferBody);
        outputStream.write(dx);
    }


        private void copyFile(Context context, Uri inputUri, Uri outputUri) throws NullPointerException,
            IOException {
        try (InputStream inputStream = context.getContentResolver().openInputStream(inputUri);
             OutputStream outputStream = new FileOutputStream(outputUri.getPath())) {
            if (inputStream == null) {
                throw new NullPointerException("InputStream for given input Uri is null");
            }
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
    }

}
