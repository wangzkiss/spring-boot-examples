package cn.vigor.modules.sys.dao;

import cn.vigor.modules.sys.entity.User;


public interface ActUserDao {
	public int insertActUserShip(User param);
	public int insertActUser(User param);
	public int updateActUser(User param);
	public int updateActPassword(User param);
    public void deleteActship(User user);
    public void deleteActUser(User user);
 }