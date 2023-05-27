package br.com.allen.controller;

import br.com.allen.dto.ProposalDetailsDTO;
import br.com.allen.service.ProposalService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/proposal")
@Authenticated
public class ProposalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProposalController.class);

    @Inject
    JsonWebToken jsonWebToken;

    @Inject
    ProposalService proposalService;

    @GET
    @Path("/{id}")
    @RolesAllowed({"user", "manager"})
    public ProposalDetailsDTO findDetailsProposal(@PathParam("id") long id) {
        LOGGER.info("Requisição recebida para obter detalhes da proposta com ID: {}", id);
        ProposalDetailsDTO fullProposal = proposalService.findFullProposal(id);
        LOGGER.info("Proposta encontrada, ID: {}", id);
        return fullProposal;
    }

    @POST
    @RolesAllowed("proposal-customer")
    public Response createProposal(ProposalDetailsDTO proposalDetails) {
        LOGGER.info("Requisição recebida para criação de proposta");
        try {
            proposalService.createNewProposal(proposalDetails);
            LOGGER.info("Proposta criada com sucesso: {}", proposalDetails);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro na tentativa de criar proposta", e);
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("manager")
    @Transactional
    public Response removeProposal(@PathParam("id") long id) {
        LOGGER.info("Requisição para remover proposta com ID: {}", id);
        try {
            proposalService.removeProposal(id);
            LOGGER.info("Proposta com ID {} removida", id);
            return Response.ok().build();
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro na deleção da proposta com ID: {}", id, e);
            return Response.serverError().build();
        }
    }
}
