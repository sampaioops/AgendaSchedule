package Conexao;

import Enums.TipoAgendamento;
import model.Evento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {
    private Connection con;


    public EventoDAO(){
        this.con = new ConnectionFactory().getConnection();
    }

    public void salvar(Evento evento){
        String sql = "INSERT INTO eventos" +
                "(titulo, data_inicio, data_final, dia_inteiro, tipo_agendamento)" +
                "VALUES(?,?,?,?,?)";



        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, evento.getTitulo());
            stmt.setTimestamp(2, new Timestamp(evento.getDataInicio().getTime()));
            stmt.setTimestamp(3, new Timestamp(evento.getDataFim().getTime()));
            stmt.setBoolean(4, evento.isDiaInteiro());
            stmt.setString(5, evento.getTipoAgendamento().getDescricao());



            stmt.execute();
            stmt.close();



        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Evento evento){
        String sql = "UPDATE eventos SET titulo= ?, data_inicio=?, data_final=?, dia_inteiro=?, tipo_agendamento=? WHERE id= ?";


        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, evento.getTitulo());
            stmt.setTimestamp(2, new Timestamp(evento.getDataInicio().getTime()));
            stmt.setTimestamp(3, new Timestamp(evento.getDataFim().getTime()));
            stmt.setBoolean(4, evento.isDiaInteiro());
            stmt.setString(5, evento.getTipoAgendamento().getDescricao());
            stmt.setLong(6, evento.getId());

            stmt.execute();
            stmt.close();



        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void remover(Evento evento){
        String sql = "DELETE FROM eventos WHERE id = ? ";

        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, evento.getId());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Evento> listar(){
        String sql = "SELECT * FROM eventos";


        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<Evento> eventos = new ArrayList<>();

            while(rs.next()) {

                Evento evento = new Evento();
                evento.setId(rs.getLong("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDataInicio(rs.getTimestamp("data_inicio"));
                evento.setDataFim(rs.getTimestamp("data_final"));
                evento.setDiaInteiro(rs.getBoolean("dia_inteiro"));
                String tipo = rs.getString("tipo_agendamento");

                if(tipo.equals("EXTERNO")){
                    evento.setTipoAgendamento(TipoAgendamento.EXTERNO);
                } else {
                    evento.setTipoAgendamento(TipoAgendamento.INTERNO);
                }

                eventos.add(evento);

            }

            rs.close();
            stmt.close();

            return eventos;
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public Evento porId(Long id){
        String sql = "SELECT * FROM eventos WHERE id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            Evento evento = new Evento();

            while(rs.next()) {

                evento.setId(rs.getLong("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDataInicio(rs.getDate("data_inicio"));
                evento.setDataFim(rs.getDate("data_final"));
                evento.setDiaInteiro(rs.getBoolean("dia_inteiro"));
                String tipo = rs.getString("tipo_agendamento");

                if(tipo.equals("EXTERNO")){
                    evento.setTipoAgendamento(TipoAgendamento.EXTERNO);
                } else {
                    evento.setTipoAgendamento(TipoAgendamento.INTERNO);
                }

            }

            rs.close();
            stmt.close();

            return evento;
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void cria(){
        String sql = "ALTER TABLE eventos ADD tipo_agendamento VARCHAR(30)";

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        EventoDAO eventoDAO = new EventoDAO();
        eventoDAO.cria();

    }



}
