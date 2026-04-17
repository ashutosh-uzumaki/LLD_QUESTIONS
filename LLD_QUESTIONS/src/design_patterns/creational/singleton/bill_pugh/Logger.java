package design_patterns.creational.singleton.bill_pugh;

class Logger {
    private Logger() {}

    private static class Holder {
        private static final Logger instance = new Logger();
    }

    public static Logger getInstance() {
        return Holder.instance;
    }
}
