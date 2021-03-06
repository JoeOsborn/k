package org.kframework.krun.ioserver.commands;

import org.kframework.krun.api.io.FileSystem;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class CommandReadbyte extends Command {


	private long ID;

	public CommandReadbyte(String[] args, Socket socket, Logger logger, FileSystem fs) { //, Long maudeId) {
		super(args, socket, logger, fs); //, maudeId);

		try {
			ID = Long.parseLong(args[1]);
		} catch (NumberFormatException nfe) {
			fail("read byte operation aborted: " + nfe.getLocalizedMessage());
		}
	}

	public void run() {
        try {
            succeed(Integer.toString((fs.get(ID).getc() & 0xff)));
        } catch (IOException e) {
            fail(e.getMessage());
        }
	}
}
