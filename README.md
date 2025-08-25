# Impressora ESC/POS Genérica

Este projeto agora utiliza uma camada genérica de envio ESC/POS para se comunicar com impressoras Bluetooth.

## Seleção de impressoras pareadas

O método `getPairedDevices()` expõe os dispositivos pareados no formato `[nome, mac]`, permitindo que o usuário escolha manualmente qual impressora utilizar.

## Compatibilidade e testes

A nova camada foi validada com diferentes modelos de impressoras ESC/POS:

- **Bematech MP-4200**
- **Epson TM-T20**
- **Elgin I9**

Foram impressos textos simples, alimentação de linhas e formatação básica (negrito, sublinhado e fontes expandidas) para garantir a compatibilidade dos comandos.

Para executar os testes, conecte a impressora via Bluetooth e utilize a aplicação de exemplo para enviar comandos de impressão.

