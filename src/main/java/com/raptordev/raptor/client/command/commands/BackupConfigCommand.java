package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.api.util.misc.ZipUtils;
import com.raptordev.raptor.client.RaptorClient;
import com.raptordev.raptor.client.command.Command;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Command.Declaration(name = "BackupConfig", syntax = "backupconfig", alias = {"backupconfig"})
public class BackupConfigCommand extends Command {

    public void onCommand(String command, String[] message) {
        String filename = "raptor-cofig-backup-" + RaptorClient.MODVER + "-" + new SimpleDateFormat("yyyyMMdd.HHmmss.SSS").format(new Date()) + ".zip";
        ZipUtils.zip(new File("RaptorClient/"), new File(filename));
        MessageBus.sendCommandMessage("Config successfully saved in " + filename + "!", true);
    }
}