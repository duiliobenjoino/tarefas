/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminTarefas.tarefas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefaDTO extends JpaRepository<Tarefa, Long> {

    Page<Tarefa> findByFinalizado(Boolean finalizado, Pageable pageable);

}
