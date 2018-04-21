package com.ariel.universalmachine.factory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.ariel.universalmachine.controller.Controller;

/**
 * Define a camada de factory dos controladores. Realiza uma especie de Singleton com os controladores.
 * ATENCAO: Os controladores são globais, portanto todos os usuarios acessaram a mesma instancia de controlador.
 * 
 * @author ariel
 *
 */
public abstract class ControllerFactory  {

	private static final Map<String, Controller> mapController = new HashMap<>();
	
	/**
	 * Retorna a instancia do controlador.
	 * @param clazz classe do controlador
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <C extends Controller> C getController(Class<C> clazz) {
		if (null == clazz) {
			return null;
		}
		
		String nome = getNomeController(clazz);
		
		C controller = ((C) mapController.get(nome));
		if (null == controller) {
			controller = instanciarController(clazz);
			mapController.put(nome, controller);
		}
		
		return controller;
	}

	/**
	 * Realiza a instancia do controlador pelo construtor sem parametros da classe controlador.
	 * @param clazz classe do controlador
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <C extends Controller> C instanciarController(Class<C> clazz) {
		for (Constructor<?> construtor : clazz.getConstructors()) {
			if (0 == construtor.getParameterCount()) {
				try {
					return (C) construtor.newInstance(new Object[] {});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * Define a chave para a classe do controlador informado.
	 * @param clazz classe do controlador
	 * @return
	 */
	private static String getNomeController(Class<?> clazz) {
		return clazz.getName();
	}
}
