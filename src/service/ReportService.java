package service;

import dao.ShipmentDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportService {

    private final ShipmentDAO shipmentDAO;

    public ReportService(ShipmentDAO shipmentDAO) {
        this.shipmentDAO = shipmentDAO;
    }

    public ResultSet getShipmentsByStatus(String status) throws SQLException {
        return shipmentDAO.getShipmentsByStatus(status);
    }

    public ResultSet getAllShipments() throws SQLException {
        return shipmentDAO.getAllShipmentsForReport();
    }
}
