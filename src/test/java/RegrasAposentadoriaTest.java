import com.AgiBank.model.ContribuicaoTotais;
import com.AgiBank.model.RegrasAposentadoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegrasAposentadoriaTest {
    @Test
    void testCalcularCoeficienteAposentadoria_AnosExtras() {
        ContribuicaoTotais contribuicao = new ContribuicaoTotais(35, 360, 72000);
        RegrasAposentadoria regras = new RegrasAposentadoria(contribuicao);

        double coeficiente = regras.calcularCoeficienteAposentadoria(30);
        assertEquals(0.70, coeficiente, 0.01); // 0.60 + (5 anos extras * 0.02)
    }

}
