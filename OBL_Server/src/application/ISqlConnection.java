package application;

import java.sql.ResultSet;

public interface ISqlConnection
{
	int executeUpdate(String msg);
	ResultSet executeQuery(String query);
}
