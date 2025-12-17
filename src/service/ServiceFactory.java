package service;

import dao.ShipmentDAO;
import dao.UserDAO;

public class ServiceFactory {

    private static ShipmentService shipmentService;
    private static LoginService loginService;
    private static DashboardService dashboardService;
    private static ReportService reportService;

    // NEW
    private static UserService userService;

    private ServiceFactory() {
        // prevent instantiation
    }

    public static ShipmentService getShipmentService() {
        if (shipmentService == null) {
            shipmentService = new SQLiteShipmentService(new ShipmentDAO());
        }
        return shipmentService;
    }

    public static LoginService getLoginService() {
        if (loginService == null) {
            loginService = new LoginService(new UserDAO());
        }
        return loginService;
    }

    public static DashboardService getDashboardService() {
        if (dashboardService == null) {
            dashboardService = new DashboardService(new ShipmentDAO());
        }
        return dashboardService;
    }

    public static ReportService getReportService() {
        if (reportService == null) {
            reportService = new ReportService(new ShipmentDAO());
        }
        return reportService;
    }

    // NEW: Factory method for UserService
    public static UserService getUserService() {
        if (userService == null) {
            userService = new UserService(new UserDAO());
        }
        return userService;
    }
}
