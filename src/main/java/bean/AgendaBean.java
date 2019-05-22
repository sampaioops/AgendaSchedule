package bean;

import Conexao.EventoDAO;
import Enums.TipoAgendamento;
import model.CostumSchedule;
import model.Evento;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import util.DateUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class AgendaBean {
    private ScheduleModel lazyEventModel;

    private Evento evento;
    private ScheduleEvent event;


    private EventoDAO eventoDAO = new EventoDAO();

    private String abreDay = "month";
    private Date data = DateUtils.asDate(LocalDate.now());

    public AgendaBean() {
        event = new CostumSchedule();
        lazyEventModel = new DefaultScheduleModel();
        evento = new Evento();
    }


    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getAbreDay() {
        return abreDay;
    }

    public void setAbreDay(String abreDay) {
        this.abreDay = abreDay;
    }


    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }


    public TipoAgendamento[] getTipoAgendamentoEnums(){

        return TipoAgendamento.values();
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

    @PostConstruct
    public void init() {
        carregaDados();
    }

    public void carregaDados(){

        lazyEventModel = new LazyScheduleModel(){

            @Override
            public void loadEvents(Date start, Date end) {

                List<Evento> eventos = eventoDAO.listar(start, end);

                for (Evento eventoAtual : eventos){



                    String tipo = "";

                    if(eventoAtual.getTipoAgendamento().equals(TipoAgendamento.EXTERNO)){
                        tipo = "externo";
                    } else {
                        tipo = "interno";
                    }

                    ScheduleEvent newEvent = new CostumSchedule(eventoAtual.getTitulo(), eventoAtual.getDataInicio(), eventoAtual.getDataFim(), eventoAtual.isDiaInteiro(), tipo, true, eventoAtual);

                    addEvent(newEvent);

                }

            }
        };
    }

    public void salvar() {

        String tipo = "";

        if(this.evento.getTipoAgendamento().equals(TipoAgendamento.EXTERNO)){
            tipo = "externo";
        } else {
            tipo = "interno";
        }

        ScheduleEvent newEvent = new CostumSchedule(this.evento.getTitulo(), this.evento.getDataInicio(), this.evento.getDataFim(), this.evento.isDiaInteiro(), tipo, true, this.evento);
        if (evento.getId() == null) {
            lazyEventModel.addEvent(newEvent);

            eventoDAO.salvar(evento);

            carregaDados();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento Salvo", "Evento Salvo");
            addMessage(message);
        } else {
            eventoDAO.atualizar(evento);
            newEvent.setId(event.getId());
            lazyEventModel.updateEvent(newEvent);

        }
    }

    public void remover() {
        lazyEventModel.deleteEvent(event);
        eventoDAO.remover(this.evento);

        carregaDados();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento Removido", "Evento Removido");
        addMessage(message);
    }

    public void atualizaTab(){
        this.abreDay = "agendaDay";
    }


    public void onDateSelect(SelectEvent selectEvent) {
        Date dataSelecionada = (Date) selectEvent.getObject();
        data.setTime(dataSelecionada.getTime());
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (CostumSchedule) selectEvent.getObject();
        this.evento = (Evento) event.getData();

    }

    public void onEventMove(ScheduleEntryMoveEvent eventoMove) {
        ScheduleEvent eventoAtt = eventoMove.getScheduleEvent();
        this.evento = (Evento) eventoAtt.getData();

        eventoDAO.atualizar(evento);
        carregaDados();

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento movido", "O evento foi atualizado!");

        addMessage(message);
    }

    public void agendar(){
        this.evento = new Evento();
        this.evento.setDataInicio(data);
        this.evento.setDataFim(data);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}