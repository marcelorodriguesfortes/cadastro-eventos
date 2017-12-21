package com.eventosapp.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventosapp.models.Convidado;
import com.eventosapp.models.Evento;
import com.eventosapp.repository.ConvidadoRepository;
import com.eventosapp.repository.EventosRepository;

@Controller
public class EventoController {
	
	/* Utilizando injeção de dependências.
	 * */
	@Autowired
	private EventosRepository er;
	
	@Autowired
	private ConvidadoRepository cr;
	
	/*O usuário requisita algo pela URL e este método
	 * retorna alguma coisa para o usuário*/
	@RequestMapping(value = "/cadastrarEvento",  method=RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}
	
	
	@RequestMapping(value = "/cadastrarEvento",  method=RequestMethod.POST)
	public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		
		//validando o objeto evento que será salvo no banco de dados
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem","Erro nas informações inseridas!");//Mensagem para o usuário (enviada para a view)
			return "redirect:/cadastrarEvento";
		}
		
		er.save(evento); //salvar o evento no banco de dados
		attributes.addFlashAttribute("mensagem","Dados inseridos com sucesso!");//Mensagem para o usuário (enviada para a view)
		
		return "redirect:/cadastrarEvento";
		
	}
	
	
	@RequestMapping("/eventos") 
	public ModelAndView listaEventos(Evento evento) {
		ModelAndView mv = new ModelAndView("index"); //Passando a página que o navegador
		Iterable<Evento> listaEventos = er.findAll();
		
		//Passando a lista de eventos para a view
		mv.addObject("eventos", listaEventos); //A página index será renderizada com os dados passados por meio da lista de eventos
		
		return mv;
	}
	
	@RequestMapping(value = "/{codigo}" , method=RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable ("codigo") long codigo) {
		
		Evento evento = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento" , evento);
		
		Iterable<Convidado> convidados = cr.findByEvento(evento);
		
		//Enviando para a view a lista recuperada do banco de dados 
		mv.addObject("convidados" , convidados);
		
		return mv;
	}
	
	@RequestMapping("/deletarEvento")
	public String deletarEvento(long codigo) {
		Evento evento = er.findByCodigo(codigo);		
		er.delete(evento);
		
		return "redirect:/eventos";
	}
	
	@RequestMapping(value = "/{codigo}" , method=RequestMethod.POST)
	public String detalhesEventoPost(@PathVariable ("codigo") long codigo, @Valid Convidado convidado, 
			BindingResult result, RedirectAttributes attributes) {
		
		//validando o objeto convidado
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", //Mensagem para o usuário (enviada para a view)
										"Erro nas informações inseridas!");
			return "redirect:/{codigo}";
		}
		Evento evento = er.findByCodigo(codigo);
		convidado.setEvento(evento);
		cr.save(convidado);
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		
		return "redirect:/{codigo}";
	}
	
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado (String rg) {
		
		Convidado convidado = cr.findByRg(rg);
		cr.delete(convidado);

		String codigo =  "" + convidado.getEvento().getCodigo();
		
		return "redirect:/" + codigo;
	}
	
	
	
}
