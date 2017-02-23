package com.hampson.salonapp.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hampson.model.Appointment;

@Repository
public class AppointmentRepository
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
 
    @Transactional(readOnly=true)
    public List<Appointment> findAll() {
        return jdbcTemplate.query("select * from Appointments", 
                new AppointmentRowMapper());
    }
 
    @Transactional(readOnly=true)
    public Appointment findUserById(int id) {
        return jdbcTemplate.queryForObject(
            "select * from Appointments where id = ?",
            new Object[]{id}, new AppointmentRowMapper());
    }
 
    public Appointment create(final Appointment appointment) 
    {
        final String sql = "insert into Appointments(name,email) values(?,?)";
 
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, appointment.getAppointmentDesc());
                ps.setString(2, appointment.getStartTime());
                ps.setString(3, appointment.getEndTime());
                return ps;
            }
        }, holder);
 
        //int newUserId = holder.getKey().intValue();
        //appointment.setId(newUserId);
        
        return appointment;
    }
}
 
class AppointmentRowMapper implements RowMapper<Appointment>
{
    @Override
    public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Appointment appointment = new Appointment(null, null, null, null, null);
        return appointment;
    }
}