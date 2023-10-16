package br.com.japsaraiva.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

    public static void copyNonNullProperties(Object source, Object target) { // static para não ter que instanciar
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source)); //Copia todas as propriedades nulas e atribui ao BeanUtils que fica rensponsável por fazer a conversão
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source); //O BeanWrapper é uma interface do proprio java que permite acessar as propriedades de um objeto

        PropertyDescriptor[] pds = src.getPropertyDescriptors(); //armazena as propriedades do objeto no caso os "parametros" de uma Task já existente
        Set<String> emptyNames = new HashSet<>();

        for(PropertyDescriptor pd: pds){
            Object srcValue = src.getPropertyValue(pd.getName()); //Verifica as propriedades ao fazer o UPDATE, caso os campos estiverem nulos faz a subscrição "overwrite"
            if(srcValue == null) {                                //dos campos vazios (campos que não foram alterados "citados no PUT") com as propriedades salvas 
                emptyNames.add(pd.getName()); //verifica
            }
        }
        
        String[] result = new String[emptyNames.size()]; //converte os valores das propriedades para um String array
        return emptyNames.toArray(result);

    }
}
