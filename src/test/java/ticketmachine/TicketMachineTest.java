package ticketmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketMachineTest {
	private static final int PRICE = 50; // Une constante

	private TicketMachine machine; // l'objet à tester

	@BeforeEach
	public void setUp() {
		machine = new TicketMachine(PRICE); // On initialise l'objet à tester
	}

	@Test
	// S1 : le prix affiché correspond à l’initialisation [cite: 79]
	void priceIsCorrectlyInitialized() {
		// Paramètres : valeur attendue, valeur effective, message si erreur
		assertEquals(PRICE, machine.getPrice(), "Initialisation incorrecte du prix");
	}

	@Test
	// S2 : la balance change quand on insère de l’argent [cite: 79]
	void insertMoneyChangesBalance() {
		// GIVEN : une machine vierge (initialisée dans @BeforeEach)
		// WHEN On insère de l'argent
		machine.insertMoney(10);
		machine.insertMoney(20);
		// THEN La balance est mise à jour, les montants sont correctement additionnés
		assertEquals(10 + 20, machine.getBalance(), "La balance n'est pas correctement mise à jour");
	}

	@Test
	// S3 : on n'imprime pas le ticket si le montant inséré est insuffisant [cite: 80]
	void printTicketFailsInsufficientMoney() {
		// GIVEN : on insère moins que le prix
		machine.insertMoney(PRICE - 1);
		// WHEN on essaie d'imprimer
		boolean result = machine.printTicket();
		// THEN l'impression échoue (false)
		assertFalse(result, "L'impression devrait échouer si montant insuffisant");
		// ET la balance reste inchangée
		assertEquals(PRICE - 1, machine.getBalance(), "La balance ne doit pas changer si l'impression échoue");
		// ET le total reste à 0
		assertEquals(0, machine.getTotal(), "Le total ne doit pas changer si l'impression échoue");
	}

	@Test
	// S4 : on imprime le ticket si le montant inséré est suffisant [cite: 81-82]
	void printTicketSuccessSufficientMoney() {
		// GIVEN : on insère exactement le prix
		machine.insertMoney(PRICE);
		// WHEN on imprime
		boolean result = machine.printTicket();
		// THEN l'impression réussit (true)
		assertTrue(result, "L'impression devrait réussir si montant suffisant");
	}

	@Test
	// S4 (bis) : on imprime le ticket si le montant inséré est supérieur
	void printTicketSuccessMoreThanSufficientMoney() {
		// GIVEN : on insère plus que le prix
		machine.insertMoney(PRICE + 20);
		// WHEN on imprime
		boolean result = machine.printTicket();
		// THEN l'impression réussit (true)
		assertTrue(result, "L'impression devrait réussir si montant supérieur");
	}

	@Test
	// S5 : Quand on imprime un ticket la balance est décrémentée du prix [cite: 83-84]
	void balanceDecrementedAfterPrint() {
		// GIVEN : on insère plus que le prix
		machine.insertMoney(PRICE + 20);
		// WHEN on imprime
		machine.printTicket();
		// THEN la balance est décrémentée du prix (il reste 20)
		assertEquals(20, machine.getBalance(), "La balance doit être décrémentée du prix du ticket");
	}

	@Test
	// S6 : le montant collecté est mis à jour quand on imprime (pas avant) [cite: 85]
	void totalUpdatedAfterPrint() {
		// GIVEN : une machine vierge
		assertEquals(0, machine.getTotal(), "Le total initial doit être 0");
		// WHEN on insère de l'argent
		machine.insertMoney(PRICE);
		// THEN le total est TOUJOURS 0 (pas encore collecté)
		assertEquals(0, machine.getTotal(), "Le total ne doit pas changer avant l'impression");
		// WHEN on imprime le ticket
		machine.printTicket();
		// THEN le total est mis à jour
		assertEquals(PRICE, machine.getTotal(), "Le total doit être mis à jour après l'impression");
	}

	@Test
	// S7 : refund() rend correctement la monnaie [cite: 86]
	void refundReturnsCorrectAmount() {
		// GIVEN : on insère de l'argent
		machine.insertMoney(30);
		// WHEN on demande le remboursement
		int refundedAmount = machine.refund();
		// THEN le montant retourné est correct
		assertEquals(30, refundedAmount, "Refund() doit retourner le montant de la balance");
	}

	@Test
	// S8 : refund() remet la balance à zéro [cite: 87]
	void refundResetsBalance() {
		// GIVEN : on insère de l'argent
		machine.insertMoney(30);
		// WHEN on demande le remboursement
		machine.refund();
		// THEN la balance de la machine est remise à 0
		assertEquals(0, machine.getBalance(), "La balance doit être remise à 0 après refund()");
	}

	@Test
	// S9 : on ne peut pas insérer un montant négatif [cite: 88]
	void insertNegativeMoneyThrowsException() {
		// WHEN on essaie d'insérer -10
		// THEN une exception est levée
		assertThrows(IllegalArgumentException.class, () -> {
			machine.insertMoney(-10);
		}, "Doit lever une exception pour un montant négatif");
	}

	@Test
	// S9 (bis) : on ne peut pas insérer un montant nul
	void insertZeroMoneyThrowsException() {
		// WHEN on essaie d'insérer 0
		// THEN une exception est levée
		assertThrows(IllegalArgumentException.class, () -> {
			machine.insertMoney(0);
		}, "Doit lever une exception pour un montant nul");
	}

	@Test
	// S10 : on ne peut pas créer de machine avec un prix négatif [cite: 89]
	void constructorNegativePriceThrowsException() {
		// WHEN on essaie de créer une machine avec un prix de -50
		// THEN une exception est levée
		assertThrows(IllegalArgumentException.class, () -> {
			new TicketMachine(-50);
		}, "Doit lever une exception pour un prix négatif");
	}

	@Test
	// S10 (bis) : on ne peut pas créer de machine avec un prix nul
	void constructorZeroPriceThrowsException() {
		// WHEN on essaie de créer une machine avec un prix de 0
		// THEN une exception est levée
		assertThrows(IllegalArgumentException.class, () -> {
			new TicketMachine(0);
		}, "Doit lever une exception pour un prix nul");
	}
}