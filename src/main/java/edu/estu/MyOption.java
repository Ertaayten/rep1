package edu.estu;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class MyOption {
    @Option(name="-task",usage = "name of the task")
    public String task;

    @Option(name="-u")
    public boolean unique;

    @Option(name="-r")
    public boolean reverse;

    @Argument(required = true)
    public String[] filename;

    @Option(name= "-start")
    public String start;

    @Option(name= "-topN")
    public int topN;
}
