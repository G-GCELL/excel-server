package com.gabia.weat.gcellexcelserver.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserLevelLock {

	private final DataSource dataSource;

	public void executeWithLock(Runnable runnable, String lockName) {
		Connection connection = DataSourceUtils.getConnection(dataSource);
		try {
			try {
				getLock(connection, lockName);
				Thread.sleep(500);
				runnable.run();
			} finally {
				releaseLock(connection, lockName);
			}
		} catch (Exception e) {
			// Failed to acquire lock
		}
	}

	private void getLock(Connection connection, String lockName) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement("select get_lock(?, ?)")) {
			preparedStatement.setString(1, lockName);
			preparedStatement.setInt(2, 0);
			checkResult(preparedStatement);
		}
	}

	private void releaseLock(Connection connection, String lockName) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement("select release_lock(?)")) {
			preparedStatement.setString(1, lockName);
			checkResult(preparedStatement);
		}
	}

	private void checkResult(PreparedStatement preparedStatement) throws SQLException {
		try (ResultSet resultSet = preparedStatement.executeQuery()) {
			if (!resultSet.next()) {
				throw new SQLException();
			}
			int result = resultSet.getInt(1);
			if (result != 1) {
				throw new SQLException();
			}
		}
	}

}
