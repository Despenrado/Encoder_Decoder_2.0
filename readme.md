## This program shows difference between Humming method encoding and trippling bits

This program simulates a signal that looks like a string of bits, divides the signal into packets, then uses two methods to encode this signal (Humming and tripling), simulates interference during signal transmission, and decodes it. The purpose of this program is to show how many signals cannot be recovered by different methods.

### Example

Created random signal of 16 bit size, the package size = 16 bit. this signal will be transmitted 100 times. Chance for interference every bit = 1%.

![/img/example.png](/img/example.png)

description of numbers from 1-st column

- Humming

```
1 - no interference => correct signal;
2 - detected interference => signal restored;
3 - detected interference in the signal and in the control bits, but signal restored => Decoder Error;
4 - detected 1 interference in the control bits or in the minor bits or Decoder Error => signal restored;
5 - detected interference in the control bits => signal cannot be restored;
6 - no interference, Decoder error = > signal cannot be restored;
7 - multiple signal interference or decoder error => signal cannot be restored;
8 - everywhere interference (in the control bits and in the signal) => signal cannot be restored;
```

- Tripling

```
1 - no interference => correct signal
2 - detected interference => signal restored;
3 - huge multiple signal interference or decoder error => signal cannot be restored;
4 - Decoder error (due to correction bits)
```

### Result example (\*.xls files in the "test" foler)

horizontal - package size (nr of bits in package)
vertical - no restored packages (signals)

Humming (interference = 1%)

![/img/hex1.png](/img/hex1.png)

Tripple (interference = 1%)

![/img/trex1.png](/img/trex1.png)
