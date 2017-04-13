INSERT INTO `domain`
  (id, uuid, owner, path)
VALUES
  (1, 'domain_uuid1', 1, 'path1');

INSERT INTO `service_offering`
  (id, cpu, ram_size)
VALUES
  (1, 2, 4096);

INSERT INTO `volumes`
  (id, domain_id, size, data_center_id, volume_type, disk_offering_id, uuid, state)
VALUES
  (1, 1, 1048576, 1, 'ROOT', 1, 'storage_uuid1', 'Ready');
