/*
 * JxBase - Copyright (c) 2019 Julien Férard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jferard.jxbase.tool;

import java.io.File;

public class LoaderArgsParser {
    public LoaderArgs parse(final String[] args) {
        if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
            System.out.println(
                    "Usage: java -cp 'path/to/jxbase/jar:path/to/jdbc/driver/jar' com.github.jferard.jxbase.tool.DatabaseLoader [option] [source] [connect_string]\n" +
                            "\n" +
                            "   -h, --help          Print this message\n" +
                            "   -c driver_class     Load the driver using Class.forName\n" +
                            "   -d                  Drop tables if they exist\n" +
                            "   -s N                Chunk size (default is one chunk of the size of the file)\n" +
                            "                       Use this to avoid an out of memory for big files\n" +
                            "   source              A directory or a single dbf file\n" +
                            "   connection_string   A connection string to the database"
            );
            return null;
        }
        boolean dropTable = false;
        int chunkSize = -1;
        String classForName = null;
        int i = 0;
        while (true) {
            if (args[i].equals("-c")) {
                classForName = args[i+1];
                i += 2;
            } else if (args[i].equals("-d")) {
                dropTable = true;
                i ++;
            } else if (args[i].equals("-s")) {
                chunkSize = Integer.parseInt(args[i+1]);
                i += 2;
            } else {
                break;
            }
        }
        final File source = new File(args[i]);
        final String url = args[i+1];
        return new LoaderArgs(source, url, dropTable, chunkSize, classForName);
    }
}
