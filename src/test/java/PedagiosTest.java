import com.AgiBank.model.Contribuicao;
import com.AgiBank.model.Pedagios;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PedagiosTest {
    @Test
    void testElegibilidadePedagio50() {
        Contribuicao c1 = new Contribuicao(1, 2000.0, LocalDate.of(2000, 1, 1), LocalDate.of(2005, 12, 31));
        Contribuicao c2 = new Contribuicao(1, 2200.0, LocalDate.of(2006, 1, 1), LocalDate.of(2010, 12, 31));

        Pedagios pedagios = new Pedagios(Arrays.asList(c1, c2), 55);
        assertTrue(pedagios.isElegivelPedagio50(), "Deve ser elegível para Pedágio 50.");
    }

    @Test
    void testElegibilidadePedagio100() {
        Contribuicao c1 = new Contribuicao(1, 2000.0, LocalDate.of(2000, 1, 1), LocalDate.of(2005, 12, 31));
        Contribuicao c2 = new Contribuicao(1, 2200.0, LocalDate.of(2006, 1, 1), LocalDate.of(2010, 12, 31));

        Pedagios pedagios = new Pedagios(Arrays.asList(c1, c2), 57);
        assertTrue(pedagios.isElegivelPedagio100(), "Deve ser elegível para Pedágio 100.");
    }

    @Test
    void testCalcularPedagio50() {
        Contribuicao c1 = new Contribuicao(1, 2000.0, LocalDate.of(2000, 1, 1), LocalDate.of(2005, 12, 31));
        Contribuicao c2 = new Contribuicao(1, 2200.0, LocalDate.of(2006, 1, 1), LocalDate.of(2010, 12, 31));

        Pedagios pedagios = new Pedagios(Arrays.asList(c1, c2), 55);

        double beneficio = pedagios.calcularPedagio50();
        assertTrue(beneficio > 0, "O benefício do Pedágio 50 deve ser maior que zero.");
    }

    @Test
    void testCalcularPedagio100() {
        Contribuicao c1 = new Contribuicao(1, 2000.0, LocalDate.of(2000, 1, 1), LocalDate.of(2005, 12, 31));
        Contribuicao c2 = new Contribuicao(1, 2200.0, LocalDate.of(2006, 1, 1), LocalDate.of(2010, 12, 31));

        Pedagios pedagios = new Pedagios(Arrays.asList(c1, c2), 55);

        double beneficio = pedagios.calcularPedagio100();
        assertTrue(beneficio > 0, "O benefício do Pedágio 100 deve ser maior que zero.");
    }
}