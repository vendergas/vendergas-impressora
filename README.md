# Impressora ESC/POS Genérica

Este projeto agora utiliza uma camada genérica de envio ESC/POS para se comunicar com impressoras Bluetooth.

## Seleção de impressoras pareadas

O método `getPairedDevices()` expõe os dispositivos pareados no formato `[nome, mac]`, permitindo que o usuário escolha manualmente qual impressora utilizar.

## Conexão automática

Ao chamar `connect(boolean keepConnection)`, a biblioteca tentará se conectar
sequencialmente a cada dispositivo pareado até que uma impressora ESC/POS
responda, dispensando a necessidade de remover outros dispositivos Bluetooth
como fones de ouvido.

## Compatibilidade e testes

A nova camada foi validada com diferentes modelos de impressoras ESC/POS:

- **Bematech MP-4200**
- **Epson TM-T20**
- **Elgin I9**
- **Leopardo A7 Light**
- **Goldensky GS-MTP8**

Foi adicionada uma estratégia de conexão com Bluetooth que tenta sockets
convencionais, inseguros e via reflexão para dar suporte a impressoras que
exigem métodos alternativos de pareamento, como os modelos Leopardo A7 Light e
Goldensky GS-MTP8.

Foram impressos textos simples, alimentação de linhas e formatação básica (negrito, sublinhado e fontes expandidas) para garantir a compatibilidade dos comandos.

Para executar os testes, conecte a impressora via Bluetooth e utilize a aplicação de exemplo para enviar comandos de impressão.

