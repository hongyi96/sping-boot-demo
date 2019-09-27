package com.example.hcnetdemo;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class FMSGCallBack implements HCNetSDK.FMSGCallBack {


    //报警信息回调函数

    @Override
    public void invoke(NativeLong lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        System.out.println("Alarmer SN is " + new String(pAlarmer.sSerialNumber));
        //DefaultTableModel alarmTableModel = ((DefaultTableModel) jTableAlarm.getModel());//获取表格模型
        String[] newRow = new String[3];
        //报警时间
        Date today = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String[] sIP = new String[2];

        StringBuilder sAlarmType = new StringBuilder("lCommand=" + lCommand.intValue());

        //lCommand是传的报警类型
        switch (lCommand.intValue()) {
            case HCNetSDK.COMM_ALARM_V30:
                HCNetSDK.NET_DVR_ALARMINFO_V30 strAlarmInfoV30 = new HCNetSDK.NET_DVR_ALARMINFO_V30();
                strAlarmInfoV30.write();
                Pointer pInfoV30 = strAlarmInfoV30.getPointer();
                pInfoV30.write(0, pAlarmInfo.getByteArray(0, strAlarmInfoV30.size()), 0, strAlarmInfoV30.size());
                strAlarmInfoV30.read();
                switch (strAlarmInfoV30.dwAlarmType) {
                    case 0:
                        sAlarmType.append("：信号量报警").append("，").append("报警输入口：").append(strAlarmInfoV30.dwAlarmInputNumber + 1);
                        break;
                    case 1:
                        sAlarmType.append("：硬盘满");
                        break;
                    case 2:
                        sAlarmType.append("：信号丢失");
                        break;
                    case 3:
                        sAlarmType.append("：移动侦测").append("，").append("报警通道：");
                        for (int i = 0; i < 64; i++) {
                            if (strAlarmInfoV30.byChannel[i] == 1) {
                                sAlarmType.append("ch").append(i + 1).append(" ");
                            }
                        }
                        break;
                    case 4:
                        sAlarmType.append("：硬盘未格式化");
                        break;
                    case 5:
                        sAlarmType.append("：读写硬盘出错");
                        break;
                    case 6:
                        sAlarmType.append("：遮挡报警");
                        break;
                    case 7:
                        sAlarmType.append("：制式不匹配");
                        break;
                    case 8:
                        sAlarmType.append("：非法访问");
                        break;
                    default:
                        break;
                }
                newRow[0] = dateFormat.format(today);
                //报警类型
                newRow[1] = sAlarmType.toString();
                //报警设备IP地址
                sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                newRow[2] = sIP[0];
                //alarmTableModel.insertRow(0, newRow);
                break;

            case HCNetSDK.COMM_UPLOAD_PLATE_RESULT:
                HCNetSDK.NET_DVR_PLATE_RESULT strPlateResult = new HCNetSDK.NET_DVR_PLATE_RESULT();
                strPlateResult.write();
                Pointer pPlateInfo = strPlateResult.getPointer();
                pPlateInfo.write(0, pAlarmInfo.getByteArray(0, strPlateResult.size()), 0, strPlateResult.size());
                strPlateResult.read();
                try {
                    String srt3 = new String(strPlateResult.struPlateInfo.sLicense, "GBK");
                    sAlarmType.append("：交通抓拍上传，车牌：").append(srt3);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

                newRow[0] = dateFormat.format(today);
                //报警类型
                newRow[1] = sAlarmType.toString();
                //报警设备IP地址
                sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                newRow[2] = sIP[0];
                break;

            case HCNetSDK.COMM_ITS_PLATE_RESULT:
                HCNetSDK.NET_ITS_PLATE_RESULT strItsPlateResult = new HCNetSDK.NET_ITS_PLATE_RESULT();
                strItsPlateResult.write();
                Pointer pItsPlateInfo = strItsPlateResult.getPointer();
                pItsPlateInfo.write(0, pAlarmInfo.getByteArray(0, strItsPlateResult.size()), 0, strItsPlateResult.size());
                strItsPlateResult.read();

                String srt3;
                try {
                    srt3 = new String(strItsPlateResult.struPlateInfo.sLicense, "GBK").trim().substring(1);
                    System.out.println("车牌号:" + srt3);


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;

            case HCNetSDK.COMM_UPLOAD_FACESNAP_RESULT:
            //case HCNetSDK.COMM_ISAPI_ALARM:
                System.out.println("人脸抓拍");
                System.out.println(Thread.currentThread().getName());
                HCNetSDK.NET_VCA_FACESNAP_RESULT strFaceSnapInfo = new HCNetSDK.NET_VCA_FACESNAP_RESULT();
                strFaceSnapInfo.write();
                Pointer pFaceSnapInfo = strFaceSnapInfo.getPointer();
                pFaceSnapInfo.write(0, pAlarmInfo.getByteArray
                        (0, strFaceSnapInfo.size()), 0, strFaceSnapInfo.size());
                strFaceSnapInfo.read();

                if (strFaceSnapInfo.dwBackgroundPicLen != 0) {
                    FileOutputStream fout;
                    try {
                        //保存图片
                        String time = String.valueOf(System.currentTimeMillis());
                        String SNCode = new String(pAlarmer.sSerialNumber).substring(29).trim();
                        System.out.println(SNCode);
                        System.out.println(SNCode + time);
                        fout = new FileOutputStream("f:\\face\\" + SNCode + time + ".jpg");
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = strFaceSnapInfo.pBuffer2.getByteBuffer(offset, strFaceSnapInfo.dwBackgroundPicLen);
                        byte[] bytes = new byte[strFaceSnapInfo.dwBackgroundPicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                /*if (strFaceSnapInfo.dwFacePicLen != 0) {
                    FileOutputStream fout;
                    try {
                        //保存图片
                        String time = String.valueOf(System.currentTimeMillis());
                        String SNCode = new String(pAlarmer.sSerialNumber).substring(29).trim();
                        System.out.println(SNCode);
                        System.out.println(SNCode + time);
                        fout = new FileOutputStream("f:\\face\\" + SNCode + time + ".jpg");
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = strFaceSnapInfo.pBuffer2.getByteBuffer(offset, strFaceSnapInfo.dwFacePicLen);
                        byte[] bytes = new byte[strFaceSnapInfo.dwFacePicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
                break;

          /*case HCNetSDK.COMM_ISAPI_ALARM:
                HCNetSDK.NET_DVR_ALARM_ISAPI_INFO isapi_info = new HCNetSDK.NET_DVR_ALARM_ISAPI_INFO();
                isapi_info.write();
                Pointer P_ISAPI = isapi_info.getPointer();
                P_ISAPI.write(0, P_ISAPI.getByteArray(0, isapi_info.size()), 0, isapi_info.size());
                isapi_info.read();
                if (isapi_info.byPicturesNumber > 0) {
                    FileOutputStream fout;
                    try {
                        fout = new FileOutputStream("f:\\face\\" + "test" + ".jpg");
                        long offset = 0;
                        ByteBuffer buffers = isapi_info.pPicPackData.getByteBuffer(offset, isapi_info.dwAlarmDataLen);
                        byte[] bytes = new byte[isapi_info.dwAlarmDataLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //将字节写入文件
                }
                break;*/

            default:
                newRow[0] = dateFormat.format(today);
                //报警类型
                newRow[1] = sAlarmType.toString();

                //报警设备IP地址
                sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                newRow[2] = sIP[0];
                //alarmTableModel.insertRow(0, newRow);
                System.out.println("未找到匹配模式");
                System.out.println(lCommand.intValue());

                break;
        }
    }


}

