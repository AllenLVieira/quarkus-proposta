package br.com.allen.service;

import br.com.allen.dto.ProposalDTO;
import br.com.allen.dto.ProposalDetailsDTO;
import br.com.allen.entity.ProposalEntity;
import br.com.allen.message.KafkaEvents;
import br.com.allen.repository.ProposalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@ApplicationScoped
public class ProposalServiceImpl implements ProposalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProposalServiceImpl.class);

    private final ProposalRepository proposalRepository;
    private final KafkaEvents kafkaEvents;

    public ProposalServiceImpl(ProposalRepository proposalRepository, KafkaEvents kafkaEvents) {
        this.proposalRepository = proposalRepository;
        this.kafkaEvents = kafkaEvents;
    }

    @Override
    public ProposalDetailsDTO findFullProposal(long id) {
        ProposalEntity proposal = proposalRepository.findById(id);
        return mapToProposalDetailsDTO(proposal);
    }

    @Override
    @Transactional
    public void createNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        ProposalDTO proposal = buildAndSaveNewProposal(proposalDetailsDTO);
        kafkaEvents.sendNewKafkaEvent(proposal);
        LOGGER.info("Criada nova proposta com ID: {}", proposal.getProposalId());
    }

    @Override
    public void removeProposal(long id) {
        proposalRepository.deleteById(id);
        LOGGER.info("Removida a proposta com ID: {}", id);
    }

    @Transactional
    public ProposalDTO buildAndSaveNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        try {
            ProposalEntity proposal = createProposalFromDetails(proposalDetailsDTO);
            proposalRepository.persist(proposal);
            LOGGER.info("Proposta salva com ID: {}", proposalDetailsDTO.getProposalId());
            return mapToProposalDTO(proposal);
        } catch (Exception e) {
            LOGGER.error("Falha ao criar proposta", e);
            throw new RuntimeException("Falha ao criar proposta", e);
        }
    }

    private ProposalEntity createProposalFromDetails(ProposalDetailsDTO proposalDetailsDTO) {
        ProposalEntity proposal = new ProposalEntity();
        proposal.setCreated(new Date());
        proposal.setProposalValidityDays(proposalDetailsDTO.getProposalValidityDays());
        proposal.setCountry(proposalDetailsDTO.getCountry());
        proposal.setCustomer(proposalDetailsDTO.getCustomer());
        proposal.setPriceTonne(proposalDetailsDTO.getPriceTonne());
        proposal.setTonnes(proposalDetailsDTO.getTonnes());
        return proposal;
    }

    private ProposalDTO mapToProposalDTO(ProposalEntity proposal) {
        return ProposalDTO.builder()
                .proposalId(proposal.getId())
                .priceTonne(proposal.getPriceTonne())
                .customer(proposal.getCustomer())
                .build();
    }

    private ProposalDetailsDTO mapToProposalDetailsDTO(ProposalEntity proposal) {
        return ProposalDetailsDTO.builder()
                .proposalId(proposal.getId())
                .proposalValidityDays(proposal.getProposalValidityDays())
                .country(proposal.getCountry())
                .priceTonne(proposal.getPriceTonne())
                .customer(proposal.getCustomer())
                .tonnes(proposal.getTonnes())
                .build();
    }
}