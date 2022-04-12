package com.fernando.rest.springjavarest.service;

import com.fernando.rest.springjavarest.model.Cliente;
import com.fernando.rest.springjavarest.model.Region;
import com.fernando.rest.springjavarest.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService{

    private ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void save(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    @Override
    public Cliente saveAndReturnCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
      return (List<Cliente>) clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public Cliente deleteById(Long id) {
        Cliente clienteDeleted = clienteRepository.findById(id).get();
        clienteRepository.delete(clienteDeleted);
        return clienteDeleted;
    }

    @Override
    public List<Cliente> findAllByRegion(Region region) {
        return clienteRepository.findAllByRegion(region);
    }
}
