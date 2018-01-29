package adminTarefas.tarefas;

import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaDTO tarefaDTO;

    @GetMapping("/nova")
    private ModelAndView nova(Tarefa tarefa) {
        ModelAndView mv = new ModelAndView("adicionarTarefa");
        if (tarefa == null) {
            tarefa = new Tarefa();
        }
        mv.addObject("tarefa", tarefa);
        return mv;
    }

    @GetMapping
    private ModelAndView listar(@RequestParam(required = false) Integer filtro, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("listarTarefas");
        Page<Tarefa> lista;
        if(filtro==null){
            filtro = 3;
        }
        switch (filtro) {
            case 1:
                lista = tarefaDTO.findByFinalizado(true, pageable);
                break;
            case 2:
                lista = tarefaDTO.findByFinalizado(false, pageable);
                break;
            default:
                lista = tarefaDTO.findAll(pageable);
                break;
        }
        mv.addObject("filtro",filtro);
        mv.addObject("pagina", new PageUtil<>(lista,request));
        return mv;
    }

    @GetMapping("/cadastro/{id}")
    private ModelAndView irParaEditar(@PathVariable("id") Long id) {
        return nova(tarefaDTO.findOne(id));
    }

    @PostMapping("/cadastro")
    private ModelAndView salvar(@Valid Tarefa tarefa, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return nova(tarefa);
        }
        tarefaDTO.save(tarefa);
        attributes.addFlashAttribute("mensagem", "Tarefa registrada com sucesso");
        return new ModelAndView("redirect:/tarefas/nova");
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        try {
            tarefaDTO.delete(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/finalizar/{id}")
    private @ResponseBody
    ResponseEntity<?> finalizar(@PathVariable("id") Tarefa tarefa) {
        tarefa.setDataFinalizacao(LocalDate.now());
        tarefa.setFinalizado(true);
        try {
            tarefaDTO.save(tarefa);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(tarefa);
    }

}
