package service;

import dao.ShipmentDAO;

import java.sql.SQLException;
import java.time.LocalDate;

public class DashboardService {

    private final ShipmentDAO shipmentDAO;

    public DashboardService(ShipmentDAO shipmentDAO) {
        this.shipmentDAO = shipmentDAO;
    }

    public int getTotalShipments() throws SQLException {
        return shipmentDAO.countAllShipments();
    }

    public int getRegisteredShipments() throws SQLException {
        return shipmentDAO.countRegisteredShipments();
    }

    public int getUpdatedShipments() throws SQLException {
        return shipmentDAO.countUpdatedShipments();
    }

    public int getTodayShipments() throws SQLException {
        String today = LocalDate.now().toString(); // "YYYY-MM-DD"
        return shipmentDAO.countTodayShipments(today);
    }
}
