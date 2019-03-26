start transaction;

use `Acme-Parade`;

revoke all privileges on `Acme-Parade`.* from 'acme-user'@'%';
revoke all privileges on `Acme-Parade`.* from 'acme-manager'@'%';

drop user 'acme-user'@'%';
drop user 'acme-manager'@'%';

drop database `Acme-Parade`;

commit;