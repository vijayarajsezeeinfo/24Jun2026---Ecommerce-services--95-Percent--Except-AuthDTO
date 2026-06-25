package com.ezeeinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezeeinfo.config.DBConfig;
import com.ezeeinfo.dto.NamespaceDTO;
import com.ezeeinfo.dto.OrderDTO;
import com.ezeeinfo.dto.PaymentDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.dto.enumeration.BillingStatusEM;
import com.ezeeinfo.dto.enumeration.OrderStatusEM;
import com.ezeeinfo.dto.enumeration.PaymentModeEM;
import com.ezeeinfo.exception.ServiceException;

@Repository
public class PaymentDAO {
	@Autowired
	UserDAO userDAO;

	private static final Logger LOG = LoggerFactory.getLogger(PaymentDAO.class);

	public List<PaymentDTO> getAllPayments(String namespaceCode) {
		List<PaymentDTO> paymentDTOs = new ArrayList<PaymentDTO>();
		String sql = "SELECT p.id AS payment_id, p.code AS payment_code, p.order_id AS payment_order_id, p.payment_mode AS payment_mode, p.total_amount_to_pay AS payment_total_amount_to_pay, p.paid_amount AS payment_paid_amount, p.balance_amount AS payment_balance_amount, p.billing_status AS payment_billing_status, p.transaction_id AS payment_transaction_id, p.remarks AS payment_remarks, p.namespace_id AS payment_namespace_id, p.active_flag AS payment_active_flag, p.updated_by AS payment_updated_by, o.id AS order_id, o.code AS order_code, o.user_id AS order_user_id, o.order_status AS order_status, o.total_amount AS order_total_amount, o.order_date AS order_date, o.namespace_id AS order_namespace_id, o.active_flag AS order_active_flag, o.updated_by AS order_updated_by, n.id AS namespace_id, n.code AS namespace_code, n.name AS namespace_name, n.active_flag AS namespace_Active_flag, n.updated_by AS namespace_updated_by FROM payments p INNER JOIN orders o ON p.order_id = o.id INNER JOIN namespace n ON p.namespace_id = n.id WHERE p.active_flag < 2 AND n.code = ?";

		try (Connection connection = DBConfig.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setString(1, namespaceCode);
			try (ResultSet rs = statement.executeQuery();) {
				while (rs.next()) {

					UserDTO namespaceUpdatedBy = userDAO.getUser(rs.getInt("namespace_updated_by"));

					NamespaceDTO namespaceDTO = new NamespaceDTO();
					namespaceDTO.setId(rs.getInt("namespace_id"));
					namespaceDTO.setCode(rs.getString("namespace_code"));
					namespaceDTO.setName(rs.getString("namespace_name"));
					namespaceDTO.setActiveFlag(rs.getInt("namespace_active_flag"));
					namespaceDTO.setUpdatedBy(namespaceUpdatedBy);

					UserDTO orderUpdatedBy = userDAO.getUser(rs.getInt("order_updated_by"));

					OrderDTO orderDTO = new OrderDTO();
					orderDTO.setId(rs.getInt("order_id"));
					orderDTO.setCode(rs.getString("order_code"));
					orderDTO.setUser(userDAO.getUser(rs.getInt("order_user_id")));
					orderDTO.setOrderStatus(OrderStatusEM.getOrderStatusEM(rs.getInt("order_status")));
					orderDTO.setTotalAmount(rs.getDouble("order_total_amount"));
					orderDTO.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
					orderDTO.setNamespace(namespaceDTO);
					orderDTO.setActiveFlag(rs.getInt("order_active_flag"));
					orderDTO.setUpdatedBy(orderUpdatedBy);

					UserDTO paymentUpdatedBy = userDAO.getUser(rs.getInt("payment_updated_by"));

					PaymentDTO paymentDTO = new PaymentDTO();
					paymentDTO.setId(rs.getInt("payment_id"));
					paymentDTO.setCode(rs.getString("payment_code"));
					paymentDTO.setOrder(orderDTO);
					paymentDTO.setPaymentMode(PaymentModeEM.getPaymentModeEM(rs.getInt("payment_mode")));
					paymentDTO.setTotalAmountToPay(rs.getDouble("payment_total_amount_to_pay"));
					paymentDTO.setPaidAmount(rs.getDouble("payment_paid_amount"));
					paymentDTO.setBalanceAmount(rs.getDouble("payment_balance_amount"));
					paymentDTO.setBillingStatus(BillingStatusEM.getBillingStatusEM(rs.getInt("payment_billing_status")));
					paymentDTO.setTransactionId(rs.getString("payment_transaction_id"));
					paymentDTO.setRemarks(rs.getString("payment_remarks"));
					paymentDTO.setNamespace(namespaceDTO);
					paymentDTO.setActiveFlag(rs.getInt("payment_active_flag"));
					paymentDTO.setUpdatedBy(paymentUpdatedBy);

					paymentDTOs.add(paymentDTO);
				}

			}
			catch (SQLException e) {
				LOG.info("SQLException while getAllPayments. {}", e);
			}
		}
		catch (SQLException e) {
			LOG.info("SQLException while getAllPayments. {}", e);
		}
		LOG.info("All Payments : {}", paymentDTOs);
		return paymentDTOs;
	}

	public PaymentDTO getPaymentByCode(String code) {
		PaymentDTO paymentDTO = null;
		String sql = "SELECT p.id AS payment_id, p.code AS payment_code, p.order_id AS payment_order_id, p.payment_mode AS payment_mode, p.total_amount_to_pay AS payment_total_amount_to_pay, p.paid_amount AS payment_paid_amount, p.balance_amount AS payment_balance_amount, p.billing_status AS payment_billing_status, p.transaction_id AS payment_transaction_id, p.remarks AS payment_remarks, p.namespace_id AS payment_namespace_id, p.active_flag AS payment_active_flag, p.updated_by AS payment_updated_by, o.id AS order_id, o.code AS order_code, o.user_id AS order_user_id, o.order_status AS order_status, o.total_amount AS order_total_amount, o.order_date AS order_date, o.namespace_id AS order_namespace_id, o.active_flag AS order_active_flag, o.updated_by AS order_updated_by, n.id AS namespace_id, n.code AS namespace_code, n.name AS namespace_name, n.active_flag AS namespace_Active_flag, n.updated_by AS namespace_updated_by FROM payments p INNER JOIN orders o ON p.order_id = o.id INNER JOIN namespace n ON p.namespace_id = n.id WHERE p.active_flag < 2 AND p.code = ?";

		try (Connection connection = DBConfig.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setString(1, code);
			try (ResultSet rs = statement.executeQuery();) {
				if (!rs.next()) {
					LOG.info("EXCEPTION 404: Payment not found for code {}", code);
					throw new ServiceException("EXCEPTION 404: Payment not found");
				}
				UserDTO namespaceUpdatedBy = userDAO.getUser(rs.getInt("namespace_updated_by"));

				NamespaceDTO namespaceDTO = new NamespaceDTO();
				namespaceDTO.setId(rs.getInt("namespace_id"));
				namespaceDTO.setCode(rs.getString("namespace_code"));
				namespaceDTO.setName(rs.getString("namespace_name"));
				namespaceDTO.setActiveFlag(rs.getInt("namespace_active_flag"));
				namespaceDTO.setUpdatedBy(namespaceUpdatedBy);

				UserDTO orderUpdatedBy = userDAO.getUser(rs.getInt("order_updated_by"));

				OrderDTO orderDTO = new OrderDTO();
				orderDTO.setId(rs.getInt("order_id"));
				orderDTO.setCode(rs.getString("order_code"));
				orderDTO.setUser(userDAO.getUser(rs.getInt("order_user_id")));
				orderDTO.setOrderStatus(OrderStatusEM.getOrderStatusEM(rs.getInt("order_status")));
				orderDTO.setTotalAmount(rs.getDouble("order_total_amount"));
				orderDTO.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
				orderDTO.setNamespace(namespaceDTO);
				orderDTO.setActiveFlag(rs.getInt("order_active_flag"));
				orderDTO.setUpdatedBy(orderUpdatedBy);

				UserDTO paymentUpdatedBy = userDAO.getUser(rs.getInt("payment_updated_by"));

				paymentDTO = new PaymentDTO();
				paymentDTO.setId(rs.getInt("payment_id"));
				paymentDTO.setCode(rs.getString("payment_code"));
				paymentDTO.setOrder(orderDTO);
				paymentDTO.setPaymentMode(PaymentModeEM.getPaymentModeEM(rs.getInt("payment_mode")));
				paymentDTO.setTotalAmountToPay(rs.getDouble("payment_total_amount_to_pay"));
				paymentDTO.setPaidAmount(rs.getDouble("payment_paid_amount"));
				paymentDTO.setBalanceAmount(rs.getDouble("payment_balance_amount"));
				paymentDTO.setBillingStatus(BillingStatusEM.getBillingStatusEM(rs.getInt("payment_billing_status")));
				paymentDTO.setTransactionId(rs.getString("payment_transaction_id"));
				paymentDTO.setRemarks(rs.getString("payment_remarks"));
				paymentDTO.setNamespace(namespaceDTO);
				paymentDTO.setActiveFlag(rs.getInt("payment_active_flag"));
				paymentDTO.setUpdatedBy(paymentUpdatedBy);

			}
			catch (SQLException e) {
				LOG.info("SQLException while getPaymentByCode. {}", e);
			}
		}
		catch (SQLException e) {
			LOG.info("SQLException while getPaymentByCode. {}", e);
		}
		LOG.info("Payment : {}", paymentDTO);
		return paymentDTO;
	}

}
