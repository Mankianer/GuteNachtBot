# GuteNachtBot
_Ein TelegramBot, um eine personalisierte GuteNachtGeschichte zu erhalten._

## RoadMap
### V0 - Technisches Grundgerüst für den Bot
- [x] Bot erstellen mit User basierter Kommunikation
- [x] User-Verwaltung via Admin-User
- [x] Timer für GuteNachtGeschichte (durch User einstellbar /timer <HH:mm>)
- [x] Dummy GuteNachtGeschichte via Text
- [x] TelegramCommandStruktur aufbauen
- [ ] Funktionalitäten via Tests absichern
- [ ] Repository einstellen
- [ ] release pipeline aufsetzen

### V1 - Custom GuteNachtGeschichten Ausgabe via Text
- [ ] GuteNachtGeschichte wird durch KI-API generiert
- [ ] User kann Prompt für GuteNachtGeschichte verwalten
- [ ] Prompt soll nur eine Beschreibung für die Erzählform sein, dies soll versucht werden zu validieren
- [ ] Es soll Prompt-Profile geben, die User auswählen können. 3 Default Profile
- [ ] Admin kann Prompt-Profile erstellen
- [ ] GutenNachtGeschichte wird aus vorgegebenen Inhalt + Prompt des Users generiert
- [ ] _optional: Sprachausgabe für Text_
 
### V2 - Gedächtnis + FeedBack-System + Gruppen-Inhalt
- [ ] FeedBack-System für TelegramBot erstellen (DialogFlow)
- [ ] User kann die Geschichte bewerten/User-FeedBack geben
- [ ] User-FeedBack wird durch KI zusammengefasst und für Prompt als Gruppen-FeedBack vorbereitet.
- [ ] User-FeedBack wird validiert und zum GuteNachtGeschichte-Abschnitt gespeichert.
- [ ] Der Bot schreibt die Geschichte als Inhalt weiter (aus Gruppen-FeedBack). 
- [ ] Der Bot erzeugt Geschichte aus (Inhalt + User-Prompt)
- [ ] Admin kann GuteNachtGeschichte neu anfangen
- [ ] Admin eigenen Start-Inhalt für GuteNachtGeschichte festlegen