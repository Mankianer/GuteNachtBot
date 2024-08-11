# GuteNachtBot
_Ein TelegramBot, um eine personalisierte GuteNachtGeschichte zu erhalten._

## RoadMap
### V0 - Technisches Grundgerüst für den Bot
- [x] Bot erstellen mit User basierter Kommunikation
- [x] User-Verwaltung via Admin-User
- [x] Timer für GuteNachtGeschichte (durch User einstellbar /timer <HH:mm>)
- [x] Dummy GuteNachtGeschichte via Text
- [x] TelegramCommandStruktur aufbauen
- [x] Repository einstellen

### V1 - Custom GuteNachtGeschichten Ausgabe via Text
- [x] GuteNachtGeschichte wird durch KI-API generiert
- [ ] User kann Customize für GuteNachtGeschichte verwalten
- [ ] Customize soll nur eine Beschreibung für die Erzählform sein, dies soll versucht werden zu validieren
- [x] Es soll Customize-Profile geben, die User auswählen können. 3 Default Profile
- [x] Admin kann Customize-Profile erstellen
- [ ] GutenNachtGeschichte wird aus vorgegebenen Inhalt + Prompt des Users generiert
- [ ] Hilfreiche Willkommensnachricht für neue User
- [ ] ci pipeline aufsetzen
- [ ] Funktionalitäten via Tests absichern
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