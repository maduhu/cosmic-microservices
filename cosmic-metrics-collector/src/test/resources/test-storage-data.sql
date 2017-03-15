INSERT INTO `domain`
  (id, uuid, owner, path)
VALUES
  (1, 'domain_uuid1', 1, 'path1');

INSERT INTO `account`
  (id, uuid, type)
VALUES
  (1, 'account_uuid1', 1);

INSERT INTO `service_offering`
  (id, cpu, ram_size)
VALUES
  (1, 2, 4096);

INSERT INTO `volumes`
  (id, account_id, domain_id, size, data_center_id, volume_type, disk_offering_id, uuid)
VALUES
  (1, 1, 1, 840105984, 1, 'ROOT', 11, 'storage_uuid1');
