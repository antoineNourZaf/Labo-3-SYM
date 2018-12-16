## Question 2.4 
Dans la manipulation ci-dessus, les tags NFC utilisés contiennent 4 valeurs textuelles codées en UTF-8 dans un format de message NDEF. Une personne malveillante ayant accès au porte-clés peut aisément copier les valeurs stockées dans celui-ci et les répliquer sur une autre puce NFC.
A partir de l’API Android concernant les tags NFC, pouvez-vous imaginer une autre approche pour rendre plus compliqué le clonage des tags NFC ? Est-ce possible sur toutes les plateformes (Android et iOS), existe-il des limitations ? Voyez-vous d’autres possibilités ?

	Toutes les informations contenues dans un tag NFC sont lisibles et donc clonable avec plus ou moins d'efficacité. Les plus faciles étant les champs textes renseignés comme celle que l'on récupère.
	Cependant, l'id du tag NFC étant attribué par le constructeur est normalement unique et immuable. Cependant, cette donnée peut quand même être lue et copiée sur des hardwares programmables tel que Proxmark ou encore sur des tags où l'on arriverait à forcer le changement de cet identifiant.
	Utilisé l'id permet d'atteindre un niveau de sécurité relativement intéressant car il faudrait avoir un accès physique au tag pour le copier. Cette solution fonctionne sur Android mais ne peut pas fonctionner sur IOS car ce dernier bloque la lecture de l'id des tag NFC.
	L'utilisation d'une clé asymétrique avec une commande pour activé un défi pour vérifier que la clé est authentique, cette solution permet de n'avoir rien d'en application comme contrôle et empêche un reverse engineering pour cracker le code.

	Source : https://stackoverflow.com/questions/22878634/how-to-prevent-nfc-tag-cloning

## Question Codes-barres/QRcode 3.2:

Points Positif et negatif des codes-barres/QRcode dans les situations suivante:
Professionnelle:
	Positif:
		Les Qrcodes sont utile dans le cardre d'information gardée dans des lieux déjà sécurisé, porte fermée etc... Dans le but de donner par exemple des accès à un serveur ou autre. 
		Une autentification type pointage peut aussi être faite avec des QRcode en en attribuant un à chaque employer.
	Negatif:
		Les Qrcodes ont un principal défaut d'être copiable facilement. Une photo peut suffir si elle est prise avec un bon appareil.
Grand public:
	Positif:
		Les Qrcodes sont très utile dans le cardre de billettrie ou de contrôle d'accès car ils sont facile a produire et peuvent tout simplement être envoyer par email au destinataire.
Ludique:
	Positif:
		L'utilisation de Qrcodes a des fin publicitaire est une des utilisations les plus simple a faire, permettant de mettre des liens sur des sites webs il devient donc très facile de faire sa publicité.
		Dans le cadre d'une preuve d'achat le Qrcodes est un outis pratique car il est possible de le mettre sur le ticket et même de le mettre en autocollant sur le produit.
Financier:
	Positif:
		Les QRcode ont le grand avantage de pouvoir être générer et transférer gratuitement, ce qui en fait une technologie très pratique dans le cardre d'une application mobile.
		
Le QRcode à un grand avantage sur le NFC par sa facilité à être créé et propagé.
Il n'est par contre pas difficile de copier un Qrcode, il est donc mieux dans un idée de sécurité de ne pas utiliser de QRcode comme unique sécurité.
	
	
## Question 5.2
Une fois la manipulation effectuée, vous constaterez que les animations de la flèche ne sont pas
fluides, il va y avoir un tremblement plus ou moins important même si le téléphone ne bouge pas.
Veuillez expliquer quelle est la cause la plus probable de ce tremblement et donner une manière (sans
forcément l’implémenter) d’y remédier.

	Le tremblement peut provenir de toutes les perturbations électromagniques engendrées par les wifis, les ondes téléphoniques ou autres. 
	Il faudrait pouvoir filtrer tous ces bruits en implémentant un filtre passe-haut dans l'application qui permettrait de n'effectuer un changement dans la flèche uniquement lorsque les changements sont suffisants.
	Source : https://fr.wikipedia.org/wiki/Filtre_passe-haut
	