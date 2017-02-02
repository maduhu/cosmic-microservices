INSERT INTO `vm_instance` (
  id, name, uuid, instance_name, state, guest_os_id, data_center_id, vnc_password, created, type, vm_type, account_id,
  domain_id, service_offering_id
) VALUES (
  1, 'vm1', 'vm_instance_uuid1', 'vm1', 'Running', 1, 1, 'vnc_password1', now(), 'User', 'vm_type1', 1, 1, 1
);

INSERT INTO `domain` (
  id, uuid, owner, path
) VALUES (
  1, 'domain_uuid1', 1, 'path1'
);

INSERT INTO `account` (
  id, uuid, type
) VALUES (
  1, 'account_uuid1', 1
);

INSERT INTO `service_offering` (
  id, cpu, ram_size
) VALUES (
  1, 2, 4096
);
